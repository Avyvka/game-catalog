import PageContainer from "@/components/layout/page-container";
import React from "react";
import PlatformList from "@/features/platforms/list";

export default async function PlatformListPage() {
  return (
    <PageContainer>
      <PlatformList />
    </PageContainer>
  );
}
