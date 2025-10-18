"use client";

import {
  MultiSelect,
  MultiSelectContent,
  MultiSelectGroup,
  MultiSelectItem,
  MultiSelectTrigger,
  MultiSelectValue
} from "@/components/ui/multi-select";

const options = [
  { value: "react", label: "React" },
  { value: "vue", label: "Vue.js" },
  { value: "angular", label: "Angular" }
];

export function DemoMultiSelect() {
  return (
    <MultiSelect>
      <MultiSelectTrigger className='w-full max-w-[400px]'>
        <MultiSelectValue placeholder='Select frameworks...' />
      </MultiSelectTrigger>
      <MultiSelectContent>
        <MultiSelectGroup>
          <MultiSelectItem value='next.js'>Next.js</MultiSelectItem>
          <MultiSelectItem value='sveltekit'>SvelteKit</MultiSelectItem>
          <MultiSelectItem value='astro'>Astro</MultiSelectItem>
          <MultiSelectItem value='vue'>Vue.js</MultiSelectItem>
        </MultiSelectGroup>
      </MultiSelectContent>
    </MultiSelect>
  );
}
