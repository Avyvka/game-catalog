"use client"

import React from "react"
import { Platform } from "@/entities"
import { useDelete } from "@refinedev/core"
import { useModalForm } from "@refinedev/react-hook-form"
import { UseModalFormReturnType } from "@refinedev/react-hook-form/"
import { useTable } from "@refinedev/react-table"
import { IconPencil, IconPlus, IconTrash } from "@tabler/icons-react"
import { ColumnDef } from "@tanstack/react-table"
import { Controller } from "react-hook-form"

import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import { Field, FieldLabel } from "@/components/ui/field"
import { Input } from "@/components/ui/input"
import { Skeleton } from "@/components/ui/skeleton"
import { RefineTable } from "@/components/table/refine-table"

export default function PlatformList() {
  const createForm = useModalForm<Platform>({
    refineCoreProps: {
      resource: "platforms",
      action: "create",
    },
  })

  const editForm = useModalForm<Platform>({
    refineCoreProps: {
      resource: "platforms",
      action: "edit",
    },
  })

  const { mutate: deleteMutate } = useDelete<Platform>({})

  const columns: ColumnDef<Platform>[] = [
    { id: "id", accessorKey: "id", header: "Id", size: 400 },
    {
      id: "name",
      accessorKey: "name",
      header: "Name",
      size: 300,
    },
    {
      id: "manufacturer",
      accessorKey: "manufacturer",
      header: "Manufacturer",
      size: -1,
    },
    {
      id: "actions",
      header: "Actions",
      cell: ({ row }) => (
        <div className="flex flex-nowrap items-center gap-2">
          <Button
            variant="ghost"
            size="icon"
            onClick={() => editForm.modal.show(row.original.id)}
          >
            <IconPencil />
          </Button>
          <Button
            variant="ghost"
            size="icon"
            onClick={() => {
              deleteMutate({ resource: "platforms", id: row.original.id })
            }}
          >
            <IconTrash className="text-red-400" />
          </Button>
        </div>
      ),
      size: 100,
    },
  ]

  const tableProps = useTable({
    columns,
    refineCoreProps: {
      resource: "platforms",
      pagination: {
        mode: "server",
      },
      sorters: {
        mode: "off",
      },
    },
    state: {
      sorting: [
        {
          id: "name",
          desc: false,
        },
      ],
    },
  })

  return (
    <div className="flex w-full flex-col gap-6">
      <div>
        <Button onClick={() => createForm.modal.show()}>
          <IconPlus />
          <span>Add new</span>
        </Button>
      </div>
      <RefineTable tableProps={tableProps as never} />
      <DialogForm form={createForm} />
      <DialogForm form={editForm} />
    </div>
  )
}

function DialogForm({ form }: { form: UseModalFormReturnType<Platform> }) {
  const {
    modal,
    control,
    handleSubmit,
    refineCore: { onFinish, formLoading },
    saveButtonProps,
    formState: { defaultValues },
  } = form
  return (
    <Dialog open={modal.visible} onOpenChange={modal.close}>
      <DialogContent onOpenAutoFocus={(e) => e.preventDefault()}>
        <DialogHeader>
          <DialogTitle>
            {defaultValues?.id ? "Edit Platform" : "Add Platform"}
          </DialogTitle>
        </DialogHeader>
        <form
          onSubmit={handleSubmit(onFinish)}
          className="mt-1 flex flex-col gap-5"
        >
          {formLoading ? (
            <div className="flex flex-col gap-3">
              <Skeleton className="h-2 w-20 rounded-md" />
              <Skeleton className="h-9 w-full rounded-md" />
            </div>
          ) : (
            <Controller
              name="name"
              control={control}
              render={({ field, fieldState }) => (
                <Field className="flex flex-col gap-3">
                  <FieldLabel>Name</FieldLabel>
                  <Input
                    {...field}
                    value={field.value ?? ""}
                    type="text"
                    className={cn(
                      fieldState.invalid &&
                        "border-destructive focus-visible:ring-destructive"
                    )}
                  />
                </Field>
              )}
            />
          )}
          {formLoading ? (
            <div className="flex flex-col gap-3">
              <Skeleton className="h-2 w-50 rounded-md" />
              <Skeleton className="h-9 w-full rounded-md" />
            </div>
          ) : (
            <Controller
              name="manufacturer"
              control={control}
              render={({ field, fieldState }) => (
                <Field className="flex flex-col gap-3">
                  <FieldLabel>Manufacturer</FieldLabel>
                  <Input
                    {...field}
                    value={field.value ?? ""}
                    type="text"
                    className={cn(
                      fieldState.invalid &&
                        "border-destructive focus-visible:ring-destructive"
                    )}
                  />
                </Field>
              )}
            />
          )}
          <DialogFooter>
            {formLoading ? (
              <Skeleton className="h-9 w-20 rounded-md" />
            ) : (
              <Button type="submit" {...saveButtonProps}>
                {defaultValues?.id ? "Update" : "Save"}
              </Button>
            )}
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}
