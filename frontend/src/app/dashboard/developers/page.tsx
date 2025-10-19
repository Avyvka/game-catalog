import PageContainer from "@/components/layout/page-container";
import React from "react";
import DeveloperList from '@/features/developers/list';

export default async function PlatformListPage() {
  return (
    <PageContainer>
      <DeveloperList />
    </PageContainer>
  );
}
