"use client";
import React from "react";
import { RefineTable } from "@/components/table/refine-table";
import { Button } from "@/components/ui/button";
import { ColumnDef } from "@tanstack/react-table";
import { IconPencil, IconPlus, IconTrash } from "@tabler/icons-react";
import { useTable } from "@refinedev/react-table";
import { Genre } from "@/entities";
import { useModalForm } from "@refinedev/react-hook-form";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Form, FormItem, FormLabel } from "@/components/ui/form";
import { UseModalFormReturnType } from "@refinedev/react-hook-form/";
import { cn } from "@/lib/utils";
import { useDelete } from "@refinedev/core";
import { Skeleton } from "@/components/ui/skeleton";

export function GenreList() {
  const createForm = useModalForm<Genre>({
    refineCoreProps: {
      resource: "genres",
      action: "create"
    }
  });

  const editForm = useModalForm<Genre>({
    refineCoreProps: {
      resource: "genres",
      action: "edit"
    }
  });

  const { mutate: deleteMutate } = useDelete<Genre>({});

  const columns: ColumnDef<Genre>[] = [
    { id: "id", accessorKey: "id", header: "Id", size: 400 },
    {
      id: "name",
      accessorKey: "name",
      header: "Name",
      size: -1
    },
    {
      id: "actions",
      header: "Actions",
      cell: ({ row }) => (
        <div className='flex flex-nowrap items-center gap-2'>
          <Button
            variant='ghost'
            size='icon'
            onClick={() => editForm.modal.show(row.original.id)}
          >
            <IconPencil />
          </Button>
          <Button
            variant='ghost'
            size='icon'
            onClick={() => {
              deleteMutate({ resource: "genres", id: row.original.id });
            }}
          >
            <IconTrash className='text-red-400' />
          </Button>
        </div>
      ),
      size: 100
    }
  ];

  const tableProps = useTable({
    columns,
    refineCoreProps: {
      resource: "genres",
      pagination: {
        mode: "server"
      },
      sorters: {
        mode: "off"
      }
    },
    state: {
      sorting: [
        {
          id: "name",
          desc: false
        }
      ]
    }
  });

  return (
    <div className='flex w-full flex-col gap-6'>
      <div>
        <Button onClick={() => createForm.modal.show()}>
          <IconPlus />
          <span>Add new</span>
        </Button>
      </div>
      <RefineTable tableProps={tableProps} />
      <DialogForm form={createForm} />
      <DialogForm form={editForm} />
    </div>
  );
}

function DialogForm({ form }: { form: UseModalFormReturnType<Genre> }) {
  const {
    modal,
    register,
    handleSubmit,
    refineCore: { onFinish, formLoading },
    saveButtonProps,
    formState: { defaultValues, errors }
  } = form;
  return (
    <Dialog open={modal.visible} onOpenChange={modal.close}>
      <DialogContent onOpenAutoFocus={(e) => e.preventDefault()}>
        <DialogHeader>
          <DialogTitle>
            {defaultValues?.id ? "Edit Genre" : "Add Genre"}
          </DialogTitle>
        </DialogHeader>
        <Form
          form={form}
          onSubmit={handleSubmit(onFinish)}
          className='mt-1 flex flex-col gap-5'
        >
          <FormItem className='flex flex-col gap-3'>
            {formLoading ? (
              <Skeleton className='h-2 w-10 rounded-md' />
            ) : (
              <FormLabel>Name</FormLabel>
            )}
            {formLoading ? (
              <Skeleton className='h-9 w-full rounded-md' />
            ) : (
              <Input
                type='text'
                {...register("name", {
                  required: true,
                  minLength: 1,
                  maxLength: 32
                })}
                className={cn(
                  errors.name &&
                    "border-destructive focus-visible:ring-destructive"
                )}
              />
            )}
          </FormItem>
          <DialogFooter>
            {formLoading ? (
              <Skeleton className='h-9 w-20 rounded-md' />
            ) : (
              <Button type='submit' {...saveButtonProps}>
                {defaultValues?.id ? "Update" : "Save"}
              </Button>
            )}
          </DialogFooter>
        </Form>
      </DialogContent>
    </Dialog>
  );
}
