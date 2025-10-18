import { Identifiable } from '@/entities';

export interface Platform extends Identifiable{
  /**
   * @minLength 0
   * @maxLength 32
   */
  name: string;
  /**
   * @minLength 0
   * @maxLength 32
   */
  manufacturer: string;
}
