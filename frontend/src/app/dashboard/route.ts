import { NextResponse } from "next/server";

export function GET(request: Request) {
  const host = request.headers.get("x-forwarded-host");

  if (host) {
    const proto = request.headers.get("x-forwarded-proto") || "http";
    return NextResponse.redirect(`${proto}://${host}/dashboard/games`);
  }

  return NextResponse.redirect(new URL("/dashboard/games", request.url));
}
