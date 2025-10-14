import { auth } from '@/auth';
import { NextRequest } from 'next/server';
import { ur } from 'zod/v4/locales';

export const revalidate = 0;
export const dynamic = 'force-dynamic';
export const fetchCache = 'force-no-store';

export {
  handler as GET,
  handler as POST,
  handler as PUT,
  handler as PATCH,
  handler as DELETE
};

async function handler(request: NextRequest) {
  const session = await auth();

  const headers = new Headers();

  request.headers.forEach((value, key) => {
    if (!key.toLowerCase().includes('cookie')) {
      headers.append(key, value);
    }
  });

  if (session?.accessToken) {
    headers.set('Authorization', `Bearer ${session.accessToken}`);
  }

  const url = `${process.env.API_BASE_URL}${request.nextUrl.pathname}${request.nextUrl.search}`;

  const options: RequestInit = {
    method: request.method,
    headers
  };

  if (request.method !== 'GET' && request.method !== 'HEAD') {
    options.body = request.body;
    (options as any).duplex = 'half';
  }

  const result = await fetch(url, options);

  return stripContentEncoding(result);
}

function stripContentEncoding(result: Response) {
  const responseHeaders = new Headers(result.headers);
  responseHeaders.delete('content-encoding');
  return new Response(result.body, {
    status: result.status,
    statusText: result.statusText,
    headers: responseHeaders
  });
}
