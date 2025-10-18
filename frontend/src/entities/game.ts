import { Identifiable, Developer, Genre, Platform } from '@/entities';

export interface Game extends Identifiable{
  /**
   * @minLength 0
   * @maxLength 32
   */
  name: string;
  developer: Developer;
  genres?: Genre[];
  platforms?: Platform[];
}
