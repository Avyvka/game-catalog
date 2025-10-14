import NextAuth from 'next-auth';
import 'next-auth/jwt';
import { JWT } from '@auth/core/jwt';
import { NextResponse } from 'next/server';

export const { handlers, auth, signIn, signOut } = NextAuth({
  providers: [
    {
      type: 'oidc',
      id: 'casdoor',
      name: 'Casdoor',
      clientId: process.env.CASDOOR_CLIENT_ID!,
      clientSecret: process.env.CASDOOR_CLIENT_SECRET!,
      authorization: {
        url: `${process.env.CASDOOR_BASE_URL}/login/oauth/authorize`,
        params: { scope: 'profile email openid' }
      },
      token: `${process.env.CASDOOR_BASE_URL}/api/login/oauth/access_token`,
      userinfo: `${process.env.CASDOOR_BASE_URL}/api/userinfo`,
      issuer: `${process.env.CASDOOR_BASE_URL}`,
      profile(profile) {
        return {
          id: profile.id,
          name: profile.name,
          email: profile.email,
          image: profile.avatar
        };
      }
    }
  ],
  callbacks: {
    jwt({ token, account }) {
      if (account) {
        return {
          ...token,
          accessToken: account.access_token,
          expiresAt: account.expires_at,
          refreshToken: account.refresh_token
        };
      } else if (token.expiresAt && Date.now() < token.expiresAt * 1000) {
        return token;
      } else {
        return refreshToken(token);
      }
    },
    session({ session, token }) {
      return { ...session, accessToken: token.accessToken, error: token.error };
    }
  }
});

async function refreshToken(token: JWT): Promise<JWT | null> {
  if (!token.refreshToken) {
    throw new TypeError('Missing refresh_token');
  }

  try {
    const response = await fetch(
      `${process.env.CASDOOR_BASE_URL}/api/login/oauth/refresh_token`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({
          client_id: process.env.CASDOOR_CLIENT_ID!,
          client_secret: process.env.CASDOOR_CLIENT_SECRET!,
          grant_type: 'refresh_token',
          refresh_token: token.refreshToken!
        })
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
      refreshToken: newTokens.refresh_token || token.refreshToken
    };
  } catch (error) {
    console.error('Error refreshing access_token', error);
    token.error = 'RefreshTokenError';
    return token;
  }
}

declare module 'next-auth' {
  interface Session {
    accessToken: string | undefined;
    error?: 'RefreshTokenError';
  }
}

declare module 'next-auth/jwt' {
  interface JWT {
    accessToken: string | undefined;
    expiresAt?: number;
    refreshToken?: string;
    error?: 'RefreshTokenError';
  }
}
