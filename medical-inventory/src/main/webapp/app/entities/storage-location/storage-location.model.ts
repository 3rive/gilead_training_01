export interface IStorageLocation {
  id: number;
  code?: string | null;
  name?: string | null;
  building?: string | null;
  temperatureControlled?: boolean | null;
}

export type NewStorageLocation = Omit<IStorageLocation, 'id'> & { id: null };
