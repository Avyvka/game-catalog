import { Identifiable } from "@/entities";

export interface Developer extends Identifiable {
  /**
   * @minLength 0
   * @maxLength 32
   */
  name: string;
  description?: string;
}
