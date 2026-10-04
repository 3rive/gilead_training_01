export interface ISupplier {
  id: number;
  name?: string | null;
  contactName?: string | null;
  email?: string | null;
  phone?: string | null;
  active?: boolean | null;
}

export type NewSupplier = Omit<ISupplier, 'id'> & { id: null };
