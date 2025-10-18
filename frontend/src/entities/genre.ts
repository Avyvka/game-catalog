import { Identifiable } from "@/entities";

export interface Genre extends Identifiable {
  /**
   * @minLength 0
   * @maxLength 32
   */
  name: string;
}
