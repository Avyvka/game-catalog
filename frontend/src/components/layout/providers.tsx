"use client"

import React from "react"
import { springDataProvider } from "@/shared/data-provider"
import { Refine } from "@refinedev/core"
import { SessionProvider } from "next-auth/react"

export default function Providers({ children }: { children: React.ReactNode }) {
  return (
    <>
      <SessionProvider>
        <Refine dataProvider={springDataProvider("/api/v1")}>{children}</Refine>
      </SessionProvider>
    </>
  )
}
