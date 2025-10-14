import PageContainer from '@/components/layout/page-container';
import { PlatformsWidget } from '@/widgets/platform/PlatformsWidget';
import React from 'react';

export default async function GamePage() {
  return (
    <PageContainer>
      <PlatformsWidget />
    </PageContainer>
  );
}
