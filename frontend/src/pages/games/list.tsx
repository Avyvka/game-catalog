"use client";
import React from "react";
import { RefineTable } from "@/components/table/refine-table";
import { Button } from "@/components/ui/button";
import { ColumnDef } from "@tanstack/react-table";
import { IconPencil, IconPlus, IconTrash } from "@tabler/icons-react";
import { useTable } from "@refinedev/react-table";
import { Developer, Game, Genre, Platform } from "@/entities";
import { useModalForm } from "@refinedev/react-hook-form";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Form, FormField, FormItem, FormLabel } from "@/components/ui/form";
import { UseModalFormReturnType } from "@refinedev/react-hook-form/";
import { cn } from "@/lib/utils";
import { useDelete, useSelect } from "@refinedev/core";
import { Skeleton } from "@/components/ui/skeleton";
import {
  Drawer,
  DrawerClose,
  DrawerContent,
  DrawerFooter,
  DrawerHeader
} from "@/components/ui/drawer";
import { useIsMobile } from "@/hooks/use-mobile";
import {
  Item,
  ItemContent,
  ItemDescription,
  ItemTitle
} from "@/components/ui/item";
import { Badge } from "@/components/ui/badge";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue
} from "@/components/ui/select";
import {
  MultiSelect,
  MultiSelectContent,
  MultiSelectGroup,
  MultiSelectItem,
  MultiSelectTrigger,
  MultiSelectValue
} from "@/components/ui/multi-select";

export function GameList() {
  const viewForm = useModalForm<Game>({
    refineCoreProps: {
      resource: "games",
      action: "edit"
    }
  });

  const createForm = useModalForm<Game>({
    refineCoreProps: {
      resource: "games",
      action: "create"
    }
  });

  const editForm = useModalForm<Game>({
    refineCoreProps: {
      resource: "games",
      action: "edit"
    }
  });

  const { mutate: deleteMutate } = useDelete<Game>({});

  const columns: ColumnDef<Game>[] = [
    { id: "id", accessorKey: "id", header: "Id", size: 400 },
    {
      id: "name",
      header: "Name",
      cell: ({ row }) => (
        <Button
          variant='link'
          className='text-foreground p-0'
          onClick={() => viewForm.modal.show(row.original.id)}
        >
          <span>{row.original.name}</span>
        </Button>
      ),
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
              deleteMutate({ resource: "games", id: row.original.id });
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
      resource: "games",
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
      <DrawerForm form={viewForm} />
      <DialogForm form={createForm} />
      <DialogForm form={editForm} />
    </div>
  );
}

function DialogForm({ form }: { form: UseModalFormReturnType<Game> }) {
  const {
    modal,
    register,
    handleSubmit,
    refineCore: { onFinish, formLoading },
    saveButtonProps,
    formState: { defaultValues, errors },
    control,
    setValue
  } = form;

  const { options: developerOptions } = useSelect<Developer>({
    resource: "developers",
    optionValue: "id",
    optionLabel: "name",
    defaultValue: defaultValues?.developer?.id
  });

  const { options: genreOptions } = useSelect<Genre>({
    resource: "genres",
    optionValue: "id",
    optionLabel: "name",
    defaultValue: defaultValues?.genres?.map((e: Genre) => e.id)
  });

  const { options: platformOptions } = useSelect<Platform>({
    resource: "platforms",
    optionValue: "id",
    optionLabel: "name",
    defaultValue: defaultValues?.platforms?.map((e: Platform) => e.id)
  });

  React.useEffect(() => {
    if (defaultValues?.developer) {
      setValue("developer", defaultValues.developer);
    }
  }, [defaultValues, setValue]);

  return (
    <Dialog open={modal.visible} onOpenChange={modal.close}>
      <DialogContent onOpenAutoFocus={(e) => e.preventDefault()}>
        <DialogHeader>
          <DialogTitle>
            {defaultValues?.id ? "Edit Game" : "Add Game"}
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

          <FormField
            name='developer'
            control={control}
            render={({ field }) => (
              <FormItem className='flex flex-col gap-3'>
                {formLoading ? (
                  <Skeleton className='h-2 w-10 rounded-md' />
                ) : (
                  <FormLabel>Developer</FormLabel>
                )}
                {formLoading ? (
                  <Skeleton className='h-9 w-full rounded-md' />
                ) : (
                  <Select
                    value={field.value?.id}
                    onValueChange={(value) => field.onChange({ id: value })}
                  >
                    <SelectTrigger className='w-full'>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      {developerOptions?.map((option) => (
                        <SelectItem
                          value={`${option.value}`}
                          key={option.value}
                        >
                          {option.label}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                )}
              </FormItem>
            )}
          />

          <FormField
            name='genres'
            control={control}
            render={({ field }) => (
              <FormItem className='flex flex-col gap-3'>
                {formLoading ? (
                  <Skeleton className='h-2 w-10 rounded-md' />
                ) : (
                  <FormLabel>Genres</FormLabel>
                )}
                {formLoading ? (
                  <Skeleton className='h-9 w-full rounded-md' />
                ) : (
                  <MultiSelect
                    values={field.value?.map((e: Platform) => e.id) ?? []}
                    onValuesChange={(values) => {
                      const selected = genreOptions
                        .filter((opt) => values.includes(opt.value))
                        .map((opt) => ({ id: opt.value }));
                      field.onChange(selected);
                    }}
                  >
                    <MultiSelectTrigger className='w-full'>
                      <MultiSelectValue />
                    </MultiSelectTrigger>
                    <MultiSelectContent>
                      <MultiSelectGroup>
                        {genreOptions?.map((option) => (
                          <MultiSelectItem
                            key={option.value}
                            value={`${option.value}`}
                          >
                            {option.label}
                          </MultiSelectItem>
                        ))}
                      </MultiSelectGroup>
                    </MultiSelectContent>
                  </MultiSelect>
                )}
              </FormItem>
            )}
          />

          <FormField
            name='platforms'
            control={control}
            render={({ field }) => (
              <FormItem className='flex flex-col gap-3'>
                {formLoading ? (
                  <Skeleton className='h-2 w-10 rounded-md' />
                ) : (
                  <FormLabel>Platforms</FormLabel>
                )}
                {formLoading ? (
                  <Skeleton className='h-9 w-full rounded-md' />
                ) : (
                  <MultiSelect
                    values={field.value?.map((e: Platform) => e.id) ?? []}
                    onValuesChange={(values) => {
                      const selected = platformOptions
                        .filter((opt) => values.includes(opt.value))
                        .map((opt) => ({ id: opt.value }));
                      field.onChange(selected);
                    }}
                  >
                    <MultiSelectTrigger className='w-full'>
                      <MultiSelectValue />
                    </MultiSelectTrigger>
                    <MultiSelectContent>
                      <MultiSelectGroup>
                        {platformOptions?.map((option) => (
                          <MultiSelectItem
                            key={option.value}
                            value={`${option.value}`}
                          >
                            {option.label}
                          </MultiSelectItem>
                        ))}
                      </MultiSelectGroup>
                    </MultiSelectContent>
                  </MultiSelect>
                )}
              </FormItem>
            )}
          />

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

function DrawerForm({ form }: { form: UseModalFormReturnType<Game> }) {
  const isMobile = useIsMobile();
  const {
    modal,
    formState: { defaultValues }
  } = form;
  const game = defaultValues as Game | null;

  return (
    <Drawer
      open={modal.visible}
      onOpenChange={modal.close}
      direction={isMobile ? "bottom" : "right"}
      handleOnly={!isMobile}
    >
      <DrawerContent>
        <DrawerHeader>
          <DialogTitle>{game?.name}</DialogTitle>
        </DrawerHeader>
        <Item>
          <ItemContent>
            <ItemTitle>Game Id</ItemTitle>
            <ItemDescription>{game?.id}</ItemDescription>
          </ItemContent>
        </Item>
        <Item>
          <ItemContent>
            <ItemTitle>Name</ItemTitle>
            <ItemDescription>{game?.name}</ItemDescription>
          </ItemContent>
        </Item>
        <Item>
          <ItemContent>
            <ItemTitle>Developer</ItemTitle>
            <ItemDescription>{game?.developer.name}</ItemDescription>
          </ItemContent>
        </Item>
        <Item>
          <ItemContent>
            <ItemTitle>Genres</ItemTitle>
            <div>
              {game?.genres &&
                game?.genres.map((genre) => (
                  <Badge
                    key={genre.id}
                    className={`${getColorByString(genre.name)}`}
                  >
                    {genre.name}
                  </Badge>
                ))}
            </div>
          </ItemContent>
        </Item>
        <Item>
          <ItemContent>
            <ItemTitle>Platforms</ItemTitle>
            <div>
              {game?.platforms &&
                game?.platforms.map((platform) => (
                  <Badge
                    key={platform.id}
                    className={`${getColorByString(platform.name)}`}
                  >
                    {platform.name}
                  </Badge>
                ))}
            </div>
          </ItemContent>
        </Item>
        <DrawerFooter>
          <DrawerClose asChild>
            <Button variant='outline'>Close</Button>
          </DrawerClose>
        </DrawerFooter>
      </DrawerContent>
    </Drawer>
  );
}

function getColorByString(str: string) {
  const colors = [
    "bg-neutral-600",
    "bg-stone-600",
    "bg-zinc-600",
    "bg-slate-600",
    "bg-gray-600",
    "bg-red-600",
    "bg-orange-600",
    "bg-amber-600",
    "bg-yellow-600",
    "bg-lime-600",
    "bg-green-600",
    "bg-emerald-600",
    "bg-teal-600",
    "bg-cyan-600",
    "bg-sky-600",
    "bg-blue-600",
    "bg-indigo-600",
    "bg-violet-600",
    "bg-purple-600",
    "bg-fuchsia-600",
    "bg-pink-600",
    "bg-rose-600"
  ];
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }
  const index = Math.abs(hash) % colors.length;
  return colors[index];
}
