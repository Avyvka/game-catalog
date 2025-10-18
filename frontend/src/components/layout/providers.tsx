'use client';

import React from 'react';
import { ActiveThemeProvider } from '../active-theme';
import { SessionProvider } from 'next-auth/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useState } from 'react';
import { Refine } from '@refinedev/core';
import { springDataProvider } from "@/shared/data-provider";
import routerProvider from "@refinedev/nextjs-router";
import { PlatformList } from "@/pages/platform/list";

export default function Providers({
  activeThemeValue,
  children
}: {
  activeThemeValue: string;
  children: React.ReactNode;
}) {
  const [queryClient] = useState(() => new QueryClient());
  return (
    <>
      <ActiveThemeProvider initialTheme={activeThemeValue}>
        <SessionProvider>
          <QueryClientProvider client={queryClient}>
            <Refine
              dataProvider={springDataProvider('/api/v1')}
              routerProvider={routerProvider}
              resources={[
                {
                  name: "platforms",
                  list: "/dashboard/platforms",
                  create: "/dashboard/platforms/create",
                  edit: "/dashboard/platforms/edit/:id",
                  show: "/dashboard/platforms/show/:id",
                  meta: {
                    canDelete: true,
                  },
                }
              ]}
            >
              {children}
            </Refine>
          </QueryClientProvider>
        </SessionProvider>
      </ActiveThemeProvider>
    </>
  );
}
