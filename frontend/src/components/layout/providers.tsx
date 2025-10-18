"use client";

import React, { useState } from "react";
import { ActiveThemeProvider } from "../active-theme";
import { SessionProvider } from "next-auth/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Refine } from "@refinedev/core";
import { springDataProvider } from "@/shared/data-provider";

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
            <Refine dataProvider={springDataProvider("/api/v1")}>
              {children}
            </Refine>
          </QueryClientProvider>
        </SessionProvider>
      </ActiveThemeProvider>
    </>
  );
}
