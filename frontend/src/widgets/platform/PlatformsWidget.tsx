'use client';
import React from 'react';
import { CrudApi, CrudTable } from '@/features/crud-table/ui/CrudTable';
import { z } from 'zod';
import { ColumnDef } from '@tanstack/react-table';
import {
  useGetById,
  useGetAll,
  useUpdate,
  useDelete,
  useCreate
} from '@/shared/api/generated/api';

const schema = z.object({
  id: z.string().optional(),
  name: z.string().min(1, "Name is required").max(32),
  manufacturer: z.string().min(1, "Manufacturer is required").max(32)
});

type PlatformDto = z.infer<typeof schema>;

const columns: ColumnDef<PlatformDto>[] = [
  {
    id: 'id',
    accessorKey: 'id',
    header: 'Id'
  },
  {
    id: 'name',
    accessorKey: 'name',
    header: 'Название'
  },
  {
    id: 'manufacturer',
    accessorKey: 'manufacturer',
    header: 'Производитель'
  }
];

const api: CrudApi<PlatformDto, string> = {
  useGetById,
  useGetAll: (page, pageSize) =>
    useGetAll({ pageable: { page, size: pageSize } }),
  useUpdate,
  useDelete,
  useCreate
};

export function PlatformsWidget() {
  return (
    <CrudTable<PlatformDto, string>
      schema={schema}
      columns={columns}
      api={api}
    />
  );
}
