"use client";

import React from "react";
import { Developer, Game, Genre, Platform } from "@/entities";
import { useDelete, useSelect } from "@refinedev/core";
import { useModalForm } from "@refinedev/react-hook-form";
import { UseModalFormReturnType } from "@refinedev/react-hook-form/";
import { useTable } from "@refinedev/react-table";
import { IconPencil, IconPlus, IconTrash } from "@tabler/icons-react";
import { ColumnDef } from "@tanstack/react-table";
import { Controller } from "react-hook-form";

import { cn } from "@/lib/utils";
import { useIsMobile } from "@/hooks/use-mobile";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  Drawer,
  DrawerClose,
  DrawerContent,
  DrawerFooter,
  DrawerHeader,
} from "@/components/ui/drawer";
import { Field, FieldLabel } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import {
  Item,
  ItemContent,
  ItemDescription,
  ItemTitle,
} from "@/components/ui/item";
import {
  MultiSelect,
  MultiSelectContent,
  MultiSelectGroup,
  MultiSelectItem,
  MultiSelectTrigger,
  MultiSelectValue,
} from "@/components/ui/multi-select";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Skeleton } from "@/components/ui/skeleton";
import { Error, RefineTable } from "@/components/table/refine-table";

export default function GameList() {
  const viewForm = useModalForm<Game>({
    refineCoreProps: {
      resource: "games",
      action: "edit",
    },
  });

  const createForm = useModalForm<Game>({
    refineCoreProps: {
      resource: "games",
      action: "create",
    },
  });

  const editForm = useModalForm<Game>({
    refineCoreProps: {
      resource: "games",
      action: "edit",
    },
  });

  const { mutate: deleteMutate } = useDelete<Game>({});

  const columns: ColumnDef<Game>[] = [
    { id: "id", accessorKey: "id", header: "Id", size: 400 },
    {
      id: "name",
      header: "Name",
      cell: ({ row }) => (
        <Button
          variant="link"
          className="text-foreground p-0"
          onClick={() => viewForm.modal.show(row.original.id)}
        >
          <span>{row.original.name}</span>
        </Button>
      ),
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
              deleteMutate({ resource: "games", id: row.original.id });
            }}
          >
            <IconTrash className="text-red-400" />
          </Button>
        </div>
      ),
      size: 100,
    },
  ];

  const tableProps = useTable({
    columns,
    refineCoreProps: {
      resource: "games",
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
  });

  const {
    refineCore: {
      tableQuery: { error, refetch },
    },
  } = tableProps;

  if (error) {
    return <Error message={error.message} onRetry={refetch} />;
  }

  return (
    <div className="flex w-full flex-col gap-6">
      <div>
        <Button onClick={() => createForm.modal.show()}>
          <IconPlus />
          <span>Add new</span>
        </Button>
      </div>
      <RefineTable tableProps={tableProps as never} />
      <DrawerForm form={viewForm} />
      <DialogForm form={createForm} />
      <DialogForm form={editForm} />
    </div>
  );
}

function DialogForm({ form }: { form: UseModalFormReturnType<Game> }) {
  const {
    modal,
    control,
    handleSubmit,
    refineCore: { onFinish, formLoading },
    saveButtonProps,
    formState: { defaultValues },
    setValue,
  } = form;

  const { options: developerOptions, query: developerQuery } =
    useSelect<Developer>({
      resource: "developers",
      optionValue: "id",
      optionLabel: "name",
      defaultValue: defaultValues?.developer?.id,
      queryOptions: {
        enabled: modal.visible,
      },
    });

  const { options: genreOptions, query: genreQuery } = useSelect<Genre>({
    resource: "genres",
    optionValue: "id",
    optionLabel: "name",
    defaultValue: defaultValues?.genres?.map((e: Genre) => e.id),
    queryOptions: {
      enabled: modal.visible,
    },
  });

  const { options: platformOptions, query: platformQuery } =
    useSelect<Platform>({
      resource: "platforms",
      optionValue: "id",
      optionLabel: "name",
      defaultValue: defaultValues?.platforms?.map((e: Platform) => e.id),
      queryOptions: {
        enabled: modal.visible,
      },
    });

  React.useEffect(() => {
    if (defaultValues?.developer) {
      setValue("developer", defaultValues.developer);
    }
  }, [defaultValues, setValue]);

  const isLoading =
    formLoading ||
    developerQuery.isLoading ||
    genreQuery.isLoading ||
    platformQuery.isLoading;

  return (
    <Dialog open={modal.visible} onOpenChange={modal.close}>
      <DialogContent onOpenAutoFocus={(e) => e.preventDefault()}>
        <DialogHeader>
          <DialogTitle>
            {defaultValues?.id ? "Edit Game" : "Add Game"}
          </DialogTitle>
        </DialogHeader>
        <form
          onSubmit={handleSubmit(onFinish)}
          className="mt-1 flex flex-col gap-5"
        >
          {isLoading ? (
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
          {isLoading ? (
            <div className="flex flex-col gap-3">
              <Skeleton className="h-2 w-32 rounded-md" />
              <Skeleton className="h-9 w-full rounded-md" />
            </div>
          ) : (
            <Controller
              name="developer"
              control={control}
              render={({ field }) => (
                <Field className="flex flex-col gap-3">
                  <FieldLabel>Developer</FieldLabel>
                  <Select
                    value={field.value?.id}
                    onValueChange={(value) => field.onChange({ id: value })}
                  >
                    <SelectTrigger className="w-full">
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
                </Field>
              )}
            />
          )}
          {isLoading ? (
            <div className="flex flex-col gap-3">
              <Skeleton className="h-2 w-16 rounded-md" />
              <Skeleton className="h-9 w-full rounded-md" />
            </div>
          ) : (
            <Controller
              name="genres"
              control={control}
              render={({ field }) => (
                <Field className="flex flex-col gap-3">
                  <FieldLabel>Genres</FieldLabel>
                  <MultiSelect
                    values={field.value?.map((e: Platform) => e.id) ?? []}
                    onValuesChange={(values) => {
                      const selected = genreOptions
                        .filter((opt) => values.includes(opt.value))
                        .map((opt) => ({ id: opt.value }));
                      field.onChange(selected);
                    }}
                  >
                    <MultiSelectTrigger className="w-full">
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
                </Field>
              )}
            />
          )}
          {isLoading ? (
            <div className="flex flex-col gap-3">
              <Skeleton className="h-2 w-24 rounded-md" />
              <Skeleton className="h-9 w-full rounded-md" />
            </div>
          ) : (
            <Controller
              name="platforms"
              control={control}
              render={({ field }) => (
                <Field className="flex flex-col gap-3">
                  <FieldLabel>Platforms</FieldLabel>
                  <MultiSelect
                    values={field.value?.map((e: Platform) => e.id) ?? []}
                    onValuesChange={(values) => {
                      const selected = platformOptions
                        .filter((opt) => values.includes(opt.value))
                        .map((opt) => ({ id: opt.value }));
                      field.onChange(selected);
                    }}
                  >
                    <MultiSelectTrigger className="w-full">
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
                </Field>
              )}
            />
          )}
          <DialogFooter>
            {isLoading ? (
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
  );
}

function DrawerForm({ form }: { form: UseModalFormReturnType<Game> }) {
  const isMobile = useIsMobile();
  const {
    modal,
    refineCore: { formLoading },
    formState: { defaultValues },
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
            <ItemDescription>
              {formLoading ? <Skeleton className="h-4 w-70" /> : game?.id}
            </ItemDescription>
          </ItemContent>
        </Item>
        <Item>
          <ItemContent>
            <ItemTitle>Name</ItemTitle>
            <ItemDescription>
              {formLoading ? <Skeleton className="h-4 w-50" /> : game?.name}
            </ItemDescription>
          </ItemContent>
        </Item>
        <Item>
          <ItemContent>
            <ItemTitle>Developer</ItemTitle>
            <ItemDescription>
              {formLoading ? (
                <Skeleton className="h-4 w-60" />
              ) : (
                game?.developer.name
              )}
            </ItemDescription>
          </ItemContent>
        </Item>
        <Item>
          <ItemContent>
            <ItemTitle>Genres</ItemTitle>
            {formLoading ? (
              <Skeleton className="h-8 w-full" />
            ) : (
              <div className={"flex flex-wrap gap-1"}>
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
            )}
          </ItemContent>
        </Item>
        <Item>
          <ItemContent>
            <ItemTitle>Platforms</ItemTitle>
            {formLoading ? (
              <Skeleton className="h-8 w-full" />
            ) : (
              <div className={"flex flex-wrap gap-1"}>
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
            )}
          </ItemContent>
        </Item>
        <DrawerFooter>
          <DrawerClose asChild>
            <Button variant="outline">Close</Button>
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
    "bg-rose-600",
  ];
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }
  const index = Math.abs(hash) % colors.length;
  return colors[index];
}
