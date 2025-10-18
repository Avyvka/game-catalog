import PageContainer from "@/components/layout/page-container";
import React from "react";
import { GenreList } from "@/pages/genres/list";

export default async function PlatformListPage() {
  return (
    <PageContainer>
      <GenreList />
    </PageContainer>
  );
}
