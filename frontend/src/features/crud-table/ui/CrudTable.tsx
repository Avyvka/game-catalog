'use client';
import * as React from 'react';
import {
  ColumnDef,
  flexRender,
  getCoreRowModel,
  useReactTable,
  Table as TanstackTable
} from '@tanstack/react-table';
import { Button } from '@/components/ui/button';
import { Label } from '@/components/ui/label';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue
} from '@/components/ui/select';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from '@/components/ui/table';
import {
  IconChevronLeft,
  IconChevronRight,
  IconChevronsLeft,
  IconChevronsRight,
  IconFile,
  IconPencil,
  IconPlus,
  IconTrash
} from '@tabler/icons-react';
import { Skeleton } from '@/components/ui/skeleton';
import { ZodType } from 'zod';
import { UseMutationResult, UseQueryResult } from '@tanstack/react-query';
import {
  Empty,
  EmptyHeader,
  EmptyMedia,
  EmptyTitle,
  EmptyDescription,
  EmptyContent
} from '@/components/ui/empty';
import { AlertCircle } from 'lucide-react';
import { CrudForm } from './CrudForm';

export interface CrudApi<T, ID> {
  useGetById: (
    id: ID
  ) => UseQueryResult<{ data: T; status: number } | undefined, unknown>;

  useGetAll: (
    page: number,
    pageSize: number
  ) => UseQueryResult<{ data: T[]; status: number } | undefined, unknown>;

  useCreate: () => UseMutationResult<
    { data: T; status: number },
    unknown,
    { data: T }
  >;

  useUpdate: () => UseMutationResult<
    { data: T; status: number },
    unknown,
    { id: ID; data: T }
  >;

  useDelete: () => UseMutationResult<{ status: number }, unknown, { id: ID }>;
}

export interface CrudTableProps<
  T,
  ID,
  TSchema extends ZodType<T> = ZodType<T>
> {
  columns: ColumnDef<T>[];
  schema: TSchema;
  api: CrudApi<T, ID>;
}

export function CrudTable<T extends { id?: ID }, ID>(
  props: CrudTableProps<T, ID>
) {
  const { api } = props;

  const [selectedItem, setSelectedItem] = React.useState<T | undefined>(
    undefined
  );
  const [isDialogOpen, setIsDialogOpen] = React.useState(false);

  const [pagination, setPagination] = React.useState({
    pageIndex: 0,
    pageSize: 10
  });

  const deleteMutation = api.useDelete();

  const columns: ColumnDef<T>[] = [
    ...props.columns,
    {
      id: 'actions',
      header: 'Actions',
      cell: ({ row }) => {
        const item = row.original;
        return (
          <div className='flex items-center gap-2'>
            <Button
              variant='ghost'
              size='icon'
              onClick={() => {
                setSelectedItem(item);
                setIsDialogOpen(true);
              }}
            >
              <IconPencil />
            </Button>
            <Button
              variant='ghost'
              size='icon'
              onClick={() => {
                deleteMutation.mutate(
                  { id: item.id! },
                  { onSuccess: () => refetch() }
                );
              }}
            >
              <IconTrash className='text-red-400' />
            </Button>
          </div>
        );
      }
    }
  ];

  const {
    data: response,
    isLoading,
    error,
    refetch
  } = api.useGetAll(pagination.pageIndex, pagination.pageSize);
  const data = response?.data || [];

  const table = useReactTable({
    data: data || [],
    columns,
    state: { pagination },
    getCoreRowModel: getCoreRowModel(),
    getRowId: (item) => String(item.id),
    onPaginationChange: setPagination,
    manualPagination: true
  });

  if (error) {
    const message =
      error && typeof (error as any).message === 'string'
        ? (error as any).message
        : undefined;
    return <Error message={message} onRetry={() => refetch()} />;
  }

  return (
    <>
      <div className='flex w-full flex-col gap-6'>
        <div>
          <Button
            variant='secondary'
            size='sm'
            onClick={() => {
              setSelectedItem(undefined);
              setIsDialogOpen(true);
            }}
          >
            <IconPlus />
            <span>Add new</span>
          </Button>
        </div>
        <div className='overflow-hidden rounded-lg border'>
          <Table>
            <TableHeader className='bg-muted sticky top-0 z-10'>
              {table.getHeaderGroups().map((hg) => (
                <TableRow key={hg.id}>
                  {hg.headers.map((header) => (
                    <TableHead key={header.id}>
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
                <SkeletonRows columns={columns} count={3} />
              ) : table.getCoreRowModel().rows?.length ? (
                table.getRowModel().rows.map((row) => (
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
                    colSpan={columns.length}
                    className='h-24 text-center'
                  >
                    No results.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </div>
        <TablePagination table={table} isLoading={isLoading} />
      </div>
      <CrudForm
        key={`${Date.now()}-${Math.random()}`}
        api={props.api}
        schema={props.schema}
        columns={props.columns}
        item={selectedItem}
        open={isDialogOpen}
        onOpenChange={setIsDialogOpen}
        onSaved={() => refetch()}
      />
    </>
  );
}

function TablePagination<T>({
  table,
  isLoading
}: {
  table: TanstackTable<T>;
  isLoading: boolean;
}) {
  return (
    <div className='flex items-center justify-between px-4'>
      <div></div>
      <div className='flex w-full items-center gap-8 lg:w-fit'>
        <div className='hidden items-center gap-2 lg:flex'>
          <Label htmlFor='rows-per-page' className='text-sm font-medium'>
            Rows per page
          </Label>
          <Select
            value={`${table.getState().pagination.pageSize}`}
            onValueChange={(value) => {
              table.setPageSize(Number(value));
            }}
            disabled={isLoading}
          >
            <SelectTrigger size='sm' className='w-20' id='rows-per-page'>
              <SelectValue placeholder={table.getState().pagination.pageSize} />
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
          Page {table.getState().pagination.pageIndex + 1} of{' '}
          {table.getPageCount()}
        </div>
        <div className='ml-auto flex items-center gap-2 lg:ml-0'>
          <Button
            variant='outline'
            className='hidden h-8 w-8 p-0 lg:flex'
            onClick={() => table.setPageIndex(0)}
            disabled={isLoading || !table.getCanPreviousPage()}
          >
            <span className='sr-only'>Go to first page</span>
            <IconChevronsLeft />
          </Button>
          <Button
            variant='outline'
            className='size-8'
            size='icon'
            onClick={() => table.previousPage()}
            disabled={isLoading || !table.getCanPreviousPage()}
          >
            <span className='sr-only'>Go to previous page</span>
            <IconChevronLeft />
          </Button>
          <Button
            variant='outline'
            className='size-8'
            size='icon'
            onClick={() => table.nextPage()}
            disabled={isLoading || !table.getCanNextPage()}
          >
            <span className='sr-only'>Go to next page</span>
            <IconChevronRight />
          </Button>
          <Button
            variant='outline'
            className='hidden size-8 lg:flex'
            size='icon'
            onClick={() => table.setPageIndex(table.getPageCount() - 1)}
            disabled={isLoading || !table.getCanNextPage()}
          >
            <span className='sr-only'>Go to last page</span>
            <IconChevronsRight />
          </Button>
        </div>
      </div>
    </div>
  );
}

function SkeletonRows<T>({
  columns,
  count
}: {
  columns: ColumnDef<T>[];
  count: number;
}) {
  return Array.from({ length: count }).map((_, i) => (
    <TableRow key={i}>
      {columns.map((_, j) => (
        <TableCell key={j} className='py-4'>
          <Skeleton className='h-3 w-full' />
        </TableCell>
      ))}
    </TableRow>
  ));
}

export function Error({
  message,
  onRetry
}: {
  message?: string;
  onRetry?: () => void;
}) {
  return (
    <Empty>
      <EmptyHeader>
        <EmptyMedia variant='icon'>
          <AlertCircle className='text-red-400' />
        </EmptyMedia>
        <EmptyTitle>An error occurred</EmptyTitle>
        <EmptyDescription>{message || 'Failed to load data'}</EmptyDescription>
      </EmptyHeader>
      {onRetry && (
        <EmptyContent>
          <Button variant='destructive' onClick={onRetry}>
            Try again
          </Button>
        </EmptyContent>
      )}
    </Empty>
  );
}
