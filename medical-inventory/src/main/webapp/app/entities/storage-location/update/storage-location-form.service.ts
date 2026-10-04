import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IStorageLocation, NewStorageLocation } from '../storage-location.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStorageLocation for edit and NewStorageLocationFormGroupInput for create.
 */
type StorageLocationFormGroupInput = IStorageLocation | PartialWithRequiredKeyOf<NewStorageLocation>;

type StorageLocationFormDefaults = Pick<NewStorageLocation, 'id' | 'temperatureControlled'>;

type StorageLocationFormGroupContent = {
  id: FormControl<IStorageLocation['id'] | NewStorageLocation['id']>;
  code: FormControl<IStorageLocation['code']>;
  name: FormControl<IStorageLocation['name']>;
  building: FormControl<IStorageLocation['building']>;
  temperatureControlled: FormControl<IStorageLocation['temperatureControlled']>;
};

export type StorageLocationFormGroup = FormGroup<StorageLocationFormGroupContent>;

@Service()
export class StorageLocationFormService {
  createStorageLocationFormGroup(storageLocation?: StorageLocationFormGroupInput): StorageLocationFormGroup {
    const storageLocationRawValue = {
      ...this.getFormDefaults(),
      ...(storageLocation ?? { id: null }),
    };

    return new FormGroup<StorageLocationFormGroupContent>({
      id: new FormControl(
        { value: storageLocationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(storageLocationRawValue.code, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(20)],
      }),
      name: new FormControl(storageLocationRawValue.name, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(80)],
      }),
      building: new FormControl(storageLocationRawValue.building, {
        validators: [Validators.maxLength(80)],
      }),
      temperatureControlled: new FormControl(storageLocationRawValue.temperatureControlled, {
        validators: [Validators.required],
      }),
    });
  }

  getStorageLocation(form: StorageLocationFormGroup): IStorageLocation | NewStorageLocation {
    return form.getRawValue();
  }

  resetForm(form: StorageLocationFormGroup, storageLocation: StorageLocationFormGroupInput): void {
    const storageLocationRawValue = { ...this.getFormDefaults(), ...storageLocation };
    form.reset({
      ...storageLocationRawValue,
      id: { value: storageLocationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): StorageLocationFormDefaults {
    return {
      id: null,
      temperatureControlled: false,
    };
  }
}
