import { signIn } from "@/auth";

export async function GET(req: Request) {
  const { searchParams } = new URL(req.url);
  return signIn("keycloak", {
    redirectTo: searchParams.get("callbackUrl") ?? ""
  });
}
