"use client";

import React, { useEffect } from "react";
import { springDataProvider } from "@/shared/data-provider";
import { HttpError, Refine } from "@refinedev/core";
import axios from "axios";
import { Session } from "next-auth";
import { SessionProvider, useSession } from "next-auth/react";

export default function Providers({
  children,
  session,
  apiUrl
}: {
  children: React.ReactNode;
  apiUrl: string;
  session?: Session | null | undefined;
}) {
  return (
    <>
      <SessionProvider session={session}>
        <RefineProvider apiUrl={apiUrl}>{children}</RefineProvider>
      </SessionProvider>
    </>
  );
}

const axiosInstance = axios.create();

axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    const customError: HttpError = {
      ...error,
      message: error.response?.data?.message,
      statusCode: error.response?.status,
    };

    return Promise.reject(customError);
  }
);

function RefineProvider({
  children,
  apiUrl,
}: {
  children: React.ReactNode;
  apiUrl: string;
}) {
  const { data: session } = useSession();

  useEffect(() => {
    const interceptor = axiosInstance.interceptors.request.use(
      (config) => {
        if (!config.headers.Authorization && session?.accessToken) {
          config.headers.Authorization = `Bearer ${session.accessToken}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    return () => {
      axiosInstance.interceptors.request.eject(interceptor);
    };
  }, [session]);

  return (
    <Refine
      dataProvider={springDataProvider(apiUrl, axiosInstance)}
      options={{
        reactQuery: {
          clientConfig: {
            defaultOptions: {
              queries: {
                staleTime: 60_000,
                refetchOnWindowFocus: false,
                refetchOnReconnect: false,
              },
            },
          },
        },
      }}
    >
      {children}
    </Refine>
  );
}
