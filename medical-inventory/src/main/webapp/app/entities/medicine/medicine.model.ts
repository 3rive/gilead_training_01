import { ICategory } from 'app/entities/category/category.model';
import { DosageForm } from 'app/entities/enumerations/dosage-form.model';
import { ISupplier } from 'app/entities/supplier/supplier.model';

export interface IMedicine {
  id: number;
  name?: string | null;
  sku?: string | null;
  genericName?: string | null;
  dosageForm?: keyof typeof DosageForm | null;
  strength?: string | null;
  unitPrice?: number | null;
  reorderLevel?: number | null;
  controlledSubstance?: boolean | null;
  description?: string | null;
  category?: Pick<ICategory, 'id' | 'name'> | null;
  supplier?: Pick<ISupplier, 'id' | 'name'> | null;
}

export type NewMedicine = Omit<IMedicine, 'id'> & { id: null };
