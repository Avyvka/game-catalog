import PageContainer from "@/components/layout/page-container";
import React from "react";
import { GameList } from "@/pages/games/list";

export default async function GamePage() {
  return (
    <PageContainer>
      <GameList />
    </PageContainer>
  );
}
