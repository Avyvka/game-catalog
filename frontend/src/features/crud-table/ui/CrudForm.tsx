'use client';
import * as React from 'react';
import { DefaultValues, Path, useForm } from 'react-hook-form';
import { CrudTableProps } from './CrudTable';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { ZodType } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';

interface CrudFormProps<T, ID> extends CrudTableProps<T, ID> {
  item: T | undefined;
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSaved?: () => void;
}

export function CrudForm<T extends { id?: ID }, ID>(
  props: CrudFormProps<T, ID>
) {
  const { columns, schema, api, item, open, onOpenChange, onSaved } = props;

  const createMutation = api.useCreate();
  const updateMutation = api.useUpdate();

  const form = useForm<T>({
    resolver: zodResolver(schema as ZodType<T, any>),
    defaultValues: (item ?? {}) as DefaultValues<T>
  });

  const getError = (field: string) =>
    (form.formState.errors as Record<string, any>)[field];

  const onSubmit = (values: T) => {
    const onSuccess = () => {
      onOpenChange(false);
      onSaved?.();
    };
    if (item) {
      updateMutation.mutate(
        { id: item.id as ID, data: values },
        {
          onSuccess: onSuccess
        }
      );
    } else {
      createMutation.mutate(
        { data: values },
        {
          onSuccess: onSuccess
        }
      );
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{item ? 'Edit' : 'Create'}</DialogTitle>
        </DialogHeader>
        <form
          className='mt-1 flex flex-col gap-5'
          onSubmit={form.handleSubmit(onSubmit)}
        >
          {columns
            .filter((column) => column.id !== 'id')
            .map((column) => {
              const field = column.id as Path<T>;
              return (
                <div key={column.id} className='flex flex-col gap-3'>
                  <Label htmlFor={column.id}>
                    {typeof column.header === 'function'
                      ? column.id
                      : column.header}
                  </Label>
                  <Input
                    id={column.id}
                    {...form.register(field)}
                    className={getError(field) ? 'border-red-500' : ''}
                  />
                </div>
              );
            })}

          <DialogFooter>
            <Button disabled={!form.formState.isValid} type='submit'>
              {item ? 'Update' : 'Save'}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
