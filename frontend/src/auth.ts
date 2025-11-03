import { JWT } from "@auth/core/jwt";
import NextAuth from "next-auth";
import Keycloak, { KeycloakProfile } from "next-auth/providers/keycloak";

export const { handlers, auth, signIn, signOut } = NextAuth({
  providers: [
    Keycloak({
      clientId: process.env.KEYCLOAK_CLIENT_ID,
      clientSecret: process.env.KEYCLOAK_CLIENT_SECRET,
      issuer: process.env.KEYCLOAK_ISSUER,
      profile: (profile: KeycloakProfile) => ({
        id: profile.sub,
        email: profile.email,
        name: profile.preferred_username,
        image: profile.picture,
      }),
    }),
  ],
  callbacks: {
    async jwt({ token, account }) {
      if (account) {
        return {
          ...token,
          idToken: account.id_token,
          accessToken: account.access_token,
          expiresAt: account.expires_at,
          refreshToken: account.refresh_token,
        };
      } else if (
        typeof token.expiresAt === "number" &&
        Date.now() < token.expiresAt * 1000
      ) {
        return token;
      } else {
        try {
          return await refreshToken(token);
        } catch {
          return null;
        }
      }
    },
    session({ session, token }) {
      return { ...session, accessToken: token.accessToken };
    },
  },
});

async function refreshToken(token: JWT): Promise<JWT> {
  if (!token.refreshToken) {
    throw new TypeError("Missing refresh_token");
  }

  const oauthToken = token as OAuthToken;

  const response = await fetch(
    `${process.env.AUTH_KEYCLOAK_ISSUER}/protocol/openid-connect/token`,
    {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: new URLSearchParams({
        client_id: process.env.AUTH_KEYCLOAK_ID!,
        client_secret: process.env.AUTH_KEYCLOAK_SECRET!,
        grant_type: "refresh_token",
        refresh_token: oauthToken.refreshToken!,
      }),
    }
  );

  const tokensOrError = await response.json();

  if (!response.ok) {
    throw tokensOrError;
  }

  const newTokens = tokensOrError as {
    access_token: string;
    expires_in: number;
    refresh_token?: string;
  };

  return {
    ...token,
    accessToken: newTokens.access_token,
    expiresAt: Math.floor(Date.now() / 1000 + newTokens.expires_in),
    refreshToken: newTokens.refresh_token || token.refreshToken,
  };
}

interface OAuthToken extends JWT {
  accessToken?: string;
  refreshToken?: string;
  expiresAt?: number;
}
