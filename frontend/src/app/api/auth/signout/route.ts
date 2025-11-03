import { NextRequest } from "next/server";
import { handlers } from "@/auth";
import { getToken } from "@auth/core/jwt";

export async function POST(request: NextRequest) {
  const token = await getToken({
    req: request,
    secret: process.env.AUTH_SECRET,
  });

  const result = await handlers.POST(request);

  if (typeof token?.idToken === "string") {
    const providerResponse = await fetch(
      `${process.env.AUTH_KEYCLOAK_ISSUER}/protocol/openid-connect/logout` +
        `?id_token_hint=${encodeURIComponent(token.idToken)}`
    );

    if (!providerResponse.ok) {
      throw providerResponse;
    }
  }

  return result;
}
