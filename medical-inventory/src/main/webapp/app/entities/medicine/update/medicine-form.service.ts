import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMedicine, NewMedicine } from '../medicine.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMedicine for edit and NewMedicineFormGroupInput for create.
 */
type MedicineFormGroupInput = IMedicine | PartialWithRequiredKeyOf<NewMedicine>;

type MedicineFormDefaults = Pick<NewMedicine, 'id' | 'controlledSubstance'>;

type MedicineFormGroupContent = {
  id: FormControl<IMedicine['id'] | NewMedicine['id']>;
  name: FormControl<IMedicine['name']>;
  sku: FormControl<IMedicine['sku']>;
  genericName: FormControl<IMedicine['genericName']>;
  dosageForm: FormControl<IMedicine['dosageForm']>;
  strength: FormControl<IMedicine['strength']>;
  unitPrice: FormControl<IMedicine['unitPrice']>;
  reorderLevel: FormControl<IMedicine['reorderLevel']>;
  controlledSubstance: FormControl<IMedicine['controlledSubstance']>;
  description: FormControl<IMedicine['description']>;
  category: FormControl<IMedicine['category']>;
  supplier: FormControl<IMedicine['supplier']>;
};

export type MedicineFormGroup = FormGroup<MedicineFormGroupContent>;

@Service()
export class MedicineFormService {
  createMedicineFormGroup(medicine?: MedicineFormGroupInput): MedicineFormGroup {
    const medicineRawValue = {
      ...this.getFormDefaults(),
      ...(medicine ?? { id: null }),
    };

    return new FormGroup<MedicineFormGroupContent>({
      id: new FormControl(
        { value: medicineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(medicineRawValue.name, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(120)],
      }),
      sku: new FormControl(medicineRawValue.sku, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(40)],
      }),
      genericName: new FormControl(medicineRawValue.genericName, {
        validators: [Validators.maxLength(120)],
      }),
      dosageForm: new FormControl(medicineRawValue.dosageForm, {
        validators: [Validators.required],
      }),
      strength: new FormControl(medicineRawValue.strength, {
        validators: [Validators.maxLength(40)],
      }),
      unitPrice: new FormControl(medicineRawValue.unitPrice, {
        validators: [Validators.required, Validators.min(0)],
      }),
      reorderLevel: new FormControl(medicineRawValue.reorderLevel, {
        validators: [Validators.required, Validators.min(0)],
      }),
      controlledSubstance: new FormControl(medicineRawValue.controlledSubstance, {
        validators: [Validators.required],
      }),
      description: new FormControl(medicineRawValue.description),
      category: new FormControl(medicineRawValue.category, {
        validators: [Validators.required],
      }),
      supplier: new FormControl(medicineRawValue.supplier),
    });
  }

  getMedicine(form: MedicineFormGroup): IMedicine | NewMedicine {
    return form.getRawValue();
  }

  resetForm(form: MedicineFormGroup, medicine: MedicineFormGroupInput): void {
    const medicineRawValue = { ...this.getFormDefaults(), ...medicine };
    form.reset({
      ...medicineRawValue,
      id: { value: medicineRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): MedicineFormDefaults {
    return {
      id: null,
      controlledSubstance: false,
    };
  }
}
