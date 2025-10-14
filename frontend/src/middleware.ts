import { auth } from "@/auth";
import { NextResponse } from "next/server";

export default auth((req) => {
  if (!req.auth || req.auth.error) {
    return NextResponse.redirect(
      new URL(
        `/api/auth/signin?callbackUrl=${encodeURIComponent(req.url)}`,
        req.url,
      ),
    );
  }
});

export const config = {
  matcher: [
    '/((?!api/auth|_next|favicon\\.ico).*)'
  ]
};
