import type { Metadata } from "next"
import NextTopLoader from "nextjs-toploader"

import { fontVariables } from "@/lib/font"
import { cn } from "@/lib/utils"
import { Toaster } from "@/components/ui/sonner"
import Providers from "@/components/layout/providers"

import "./globals.css"
import "./theme.css"

import { ReactNode } from "react"

import { ActiveThemeProvider } from "@/components/active-theme"
import { ThemeProvider } from "@/components/theme-provider"
import { auth } from "@/auth";
import { useSession } from "next-auth/react";

const META_THEME_COLORS = {
  light: "#ffffff",
  dark: "#09090b",
}

export const metadata: Metadata = {
  title: "Game Catalog",
  description: "Game Catalog",
}

export default async function RootLayout({
  children,
}: Readonly<{
  children: ReactNode
}>) {
  const session = await auth();
  return (
    <html lang="en" suppressHydrationWarning>
      <head>
        <script
          dangerouslySetInnerHTML={{
            __html: `
              try {
                if (localStorage.theme === 'dark' || ((!('theme' in localStorage) || localStorage.theme === 'system') && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
                  document.querySelector('meta[name="theme-color"]').setAttribute('content', '${META_THEME_COLORS.dark}')
                }
                if (localStorage.layout) {
                  document.documentElement.classList.add('layout-' + localStorage.layout)
                }
              } catch (_) {}
            `,
          }}
        />
        <meta name="theme-color" content={META_THEME_COLORS.light} />
        <title></title>
      </head>
      <body
        className={cn(
          "text-foreground group/body theme-blue overscroll-none font-sans antialiased [--footer-height:calc(var(--spacing)*14)] [--header-height:calc(var(--spacing)*14)] xl:[--footer-height:calc(var(--spacing)*24)]",
          fontVariables
        )}
      >
        <NextTopLoader color="var(--primary)" showSpinner={false} />
        <ThemeProvider>
          <ActiveThemeProvider initialTheme="blue">
            <Providers session={session}>
              <Toaster position="top-center" />
              {children}
            </Providers>
          </ActiveThemeProvider>
        </ThemeProvider>
      </body>
    </html>
  )
}
