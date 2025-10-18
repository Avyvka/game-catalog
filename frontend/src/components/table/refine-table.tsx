import React from "react";
import { flexRender } from "@tanstack/react-table";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from "../ui/table";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue
} from "../ui/select";
import { UseTableReturnType } from "@refinedev/react-table";
import { Skeleton } from "@/components/ui/skeleton";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import {
  IconChevronLeft,
  IconChevronRight,
  IconChevronsLeft,
  IconChevronsRight
} from "@tabler/icons-react";

export function RefineTable({
  tableProps
}: {
  tableProps: UseTableReturnType<any, any>;
}) {
  const {
    reactTable: {
      getHeaderGroups,
      getAllColumns,
      getSortedRowModel,
      getRowModel,
      getState,
      setPageSize,
      getPageCount,
      getCanNextPage,
      getCanPreviousPage,
      previousPage,
      nextPage,
      setPageIndex
    },
    refineCore: {
      tableQuery: { isLoading }
    }
  } = tableProps;

  return (
    <div className='flex w-full flex-col gap-6'>
      <div className='overflow-hidden rounded-lg border'>
        <Table>
          <TableHeader className='bg-muted sticky top-0 z-10'>
            {getHeaderGroups().map((hg) => (
              <TableRow key={hg.id}>
                {hg.headers.map((header) => (
                  <TableHead
                    key={header.id}
                    style={{
                      width:
                        header.column.columnDef.size !== -1
                          ? `${header.column.columnDef.size}px`
                          : "auto"
                    }}
                  >
                    {flexRender(
                      header.column.columnDef.header,
                      header.getContext()
                    )}
                  </TableHead>
                ))}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {isLoading ? (
              <SkeletonRows rows={3} columns={getAllColumns().length} />
            ) : getSortedRowModel().rows?.length ? (
              getRowModel().rows.map((row) => (
                <TableRow key={row.id}>
                  {row.getVisibleCells().map((cell) => (
                    <TableCell key={cell.id}>
                      {flexRender(
                        cell.column.columnDef.cell,
                        cell.getContext()
                      )}
                    </TableCell>
                  ))}
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell
                  colSpan={getAllColumns().length}
                  className='h-24 text-center'
                >
                  No results.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <div className='flex items-center justify-between px-4'>
        <div></div>
        <div className='flex w-full items-center gap-8 lg:w-fit'>
          <div className='hidden items-center gap-2 lg:flex'>
            <Label htmlFor='rows-per-page' className='text-sm font-medium'>
              Rows per page
            </Label>
            <Select
              value={`${getState().pagination.pageSize}`}
              onValueChange={(value) => {
                setPageSize(Number(value));
              }}
              disabled={isLoading}
            >
              <SelectTrigger size='sm' className='w-20' id='rows-per-page'>
                <SelectValue placeholder={getState().pagination.pageSize} />
              </SelectTrigger>
              <SelectContent side='top'>
                {[10, 20, 30, 40, 50].map((pageSize) => (
                  <SelectItem key={pageSize} value={`${pageSize}`}>
                    {pageSize}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
          <div className='flex w-fit items-center justify-center text-sm font-medium'>
            Page {getState().pagination.pageIndex + 1} of {getPageCount()}
          </div>
          <div className='ml-auto flex items-center gap-2 lg:ml-0'>
            <Button
              variant='outline'
              className='hidden h-8 w-8 p-0 lg:flex'
              onClick={() => setPageIndex(0)}
              disabled={isLoading || !getCanPreviousPage()}
            >
              <span className='sr-only'>Go to first page</span>
              <IconChevronsLeft />
            </Button>
            <Button
              variant='outline'
              className='size-8'
              size='icon'
              onClick={() => previousPage()}
              disabled={isLoading || !getCanPreviousPage()}
            >
              <span className='sr-only'>Go to previous page</span>
              <IconChevronLeft />
            </Button>
            <Button
              variant='outline'
              className='size-8'
              size='icon'
              onClick={() => nextPage()}
              disabled={isLoading || !getCanNextPage()}
            >
              <span className='sr-only'>Go to next page</span>
              <IconChevronRight />
            </Button>
            <Button
              variant='outline'
              className='hidden size-8 lg:flex'
              size='icon'
              onClick={() => setPageIndex(getPageCount() - 1)}
              disabled={isLoading || !getCanNextPage()}
            >
              <span className='sr-only'>Go to last page</span>
              <IconChevronsRight />
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}

function SkeletonRows({ rows, columns }: { rows: number; columns: number }) {
  return Array.from({ length: rows }).map((_, i) => (
    <TableRow key={i}>
      {Array.from({ length: columns }).map((_, j) => (
        <TableCell key={j} className='py-4'>
          <Skeleton className='h-3 w-full' />
        </TableCell>
      ))}
    </TableRow>
  ));
}
