import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISupplier, NewSupplier } from '../supplier.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISupplier for edit and NewSupplierFormGroupInput for create.
 */
type SupplierFormGroupInput = ISupplier | PartialWithRequiredKeyOf<NewSupplier>;

type SupplierFormDefaults = Pick<NewSupplier, 'id' | 'active'>;

type SupplierFormGroupContent = {
  id: FormControl<ISupplier['id'] | NewSupplier['id']>;
  name: FormControl<ISupplier['name']>;
  contactName: FormControl<ISupplier['contactName']>;
  email: FormControl<ISupplier['email']>;
  phone: FormControl<ISupplier['phone']>;
  active: FormControl<ISupplier['active']>;
};

export type SupplierFormGroup = FormGroup<SupplierFormGroupContent>;

@Service()
export class SupplierFormService {
  createSupplierFormGroup(supplier?: SupplierFormGroupInput): SupplierFormGroup {
    const supplierRawValue = {
      ...this.getFormDefaults(),
      ...(supplier ?? { id: null }),
    };

    return new FormGroup<SupplierFormGroupContent>({
      id: new FormControl(
        { value: supplierRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(supplierRawValue.name, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(120)],
      }),
      contactName: new FormControl(supplierRawValue.contactName, {
        validators: [Validators.maxLength(80)],
      }),
      email: new FormControl(supplierRawValue.email, {
        validators: [Validators.maxLength(100)],
      }),
      phone: new FormControl(supplierRawValue.phone, {
        validators: [Validators.maxLength(30)],
      }),
      active: new FormControl(supplierRawValue.active, {
        validators: [Validators.required],
      }),
    });
  }

  getSupplier(form: SupplierFormGroup): ISupplier | NewSupplier {
    return form.getRawValue();
  }

  resetForm(form: SupplierFormGroup, supplier: SupplierFormGroupInput): void {
    const supplierRawValue = { ...this.getFormDefaults(), ...supplier };
    form.reset({
      ...supplierRawValue,
      id: { value: supplierRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): SupplierFormDefaults {
    return {
      id: null,
      active: false,
    };
  }
}
