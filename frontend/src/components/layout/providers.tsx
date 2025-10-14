'use client';

import React from 'react';
import { ActiveThemeProvider } from '../active-theme';
import { SessionProvider } from 'next-auth/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { useState } from 'react'

export default function Providers({
  activeThemeValue,
  children
}: {
  activeThemeValue: string;
  children: React.ReactNode;
}) {
  const [queryClient] = useState(() => new QueryClient())
  return (
    <>
      <ActiveThemeProvider initialTheme={activeThemeValue}>
          <SessionProvider>
            <QueryClientProvider client={queryClient}>
              {children}
            </QueryClientProvider>
          </SessionProvider>
      </ActiveThemeProvider>
    </>
  );
}
