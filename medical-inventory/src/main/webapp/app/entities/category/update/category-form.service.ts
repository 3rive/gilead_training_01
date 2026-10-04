import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICategory, NewCategory } from '../category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategory for edit and NewCategoryFormGroupInput for create.
 */
type CategoryFormGroupInput = ICategory | PartialWithRequiredKeyOf<NewCategory>;

type CategoryFormDefaults = Pick<NewCategory, 'id'>;

type CategoryFormGroupContent = {
  id: FormControl<ICategory['id'] | NewCategory['id']>;
  name: FormControl<ICategory['name']>;
  description: FormControl<ICategory['description']>;
};

export type CategoryFormGroup = FormGroup<CategoryFormGroupContent>;

@Service()
export class CategoryFormService {
  createCategoryFormGroup(category?: CategoryFormGroupInput): CategoryFormGroup {
    const categoryRawValue = {
      ...this.getFormDefaults(),
      ...(category ?? { id: null }),
    };

    return new FormGroup<CategoryFormGroupContent>({
      id: new FormControl(
        { value: categoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(categoryRawValue.name, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(80)],
      }),
      description: new FormControl(categoryRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
    });
  }

  getCategory(form: CategoryFormGroup): ICategory | NewCategory {
    return form.getRawValue();
  }

  resetForm(form: CategoryFormGroup, category: CategoryFormGroupInput): void {
    const categoryRawValue = { ...this.getFormDefaults(), ...category };
    form.reset({
      ...categoryRawValue,
      id: { value: categoryRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CategoryFormDefaults {
    return {
      id: null,
    };
  }
}
