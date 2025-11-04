import { NextRequest } from "next/server";
import { handlers } from "@/auth";
import { getToken } from "@auth/core/jwt";

export async function POST(request: NextRequest) {
  const token = await getToken({
    req: request,
    secret: process.env.AUTH_SECRET,
  });

  console.log(typeof token?.refreshToken === "string");

  const result = await handlers.POST(request);

  if (typeof token?.refreshToken === "string") {
    const providerResponse = await fetch(
      `${process.env.AUTH_KEYCLOAK_ISSUER}/protocol/openid-connect/logout`,
      {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({
          client_id: process.env.AUTH_KEYCLOAK_ID!,
          client_secret: process.env.AUTH_KEYCLOAK_SECRET!,
          refresh_token: token.refreshToken,
        }),
      }
    );

    console.log(providerResponse);

    if (!providerResponse.ok) {
      throw providerResponse;
    }
  }

  return result;
}
