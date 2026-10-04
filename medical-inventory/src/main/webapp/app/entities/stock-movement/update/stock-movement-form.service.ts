import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IStockMovement, NewStockMovement } from '../stock-movement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStockMovement for edit and NewStockMovementFormGroupInput for create.
 */
type StockMovementFormGroupInput = IStockMovement | PartialWithRequiredKeyOf<NewStockMovement>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IStockMovement | NewStockMovement> = Omit<T, 'occurredAt'> & {
  occurredAt?: string | null;
};

type StockMovementFormRawValue = FormValueOf<IStockMovement>;

type NewStockMovementFormRawValue = FormValueOf<NewStockMovement>;

type StockMovementFormDefaults = Pick<NewStockMovement, 'id' | 'occurredAt'>;

type StockMovementFormGroupContent = {
  id: FormControl<StockMovementFormRawValue['id'] | NewStockMovement['id']>;
  movementType: FormControl<StockMovementFormRawValue['movementType']>;
  quantity: FormControl<StockMovementFormRawValue['quantity']>;
  occurredAt: FormControl<StockMovementFormRawValue['occurredAt']>;
  reason: FormControl<StockMovementFormRawValue['reason']>;
  referenceNumber: FormControl<StockMovementFormRawValue['referenceNumber']>;
  stockLot: FormControl<StockMovementFormRawValue['stockLot']>;
};

export type StockMovementFormGroup = FormGroup<StockMovementFormGroupContent>;

@Service()
export class StockMovementFormService {
  createStockMovementFormGroup(stockMovement?: StockMovementFormGroupInput): StockMovementFormGroup {
    const stockMovementRawValue = this.convertStockMovementToStockMovementRawValue({
      ...this.getFormDefaults(),
      ...(stockMovement ?? { id: null }),
    });

    return new FormGroup<StockMovementFormGroupContent>({
      id: new FormControl(
        { value: stockMovementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      movementType: new FormControl(stockMovementRawValue.movementType, {
        validators: [Validators.required],
      }),
      quantity: new FormControl(stockMovementRawValue.quantity, {
        validators: [Validators.required],
      }),
      occurredAt: new FormControl(stockMovementRawValue.occurredAt, {
        validators: [Validators.required],
      }),
      reason: new FormControl(stockMovementRawValue.reason, {
        validators: [Validators.maxLength(255)],
      }),
      referenceNumber: new FormControl(stockMovementRawValue.referenceNumber, {
        validators: [Validators.maxLength(40)],
      }),
      stockLot: new FormControl(stockMovementRawValue.stockLot, {
        validators: [Validators.required],
      }),
    });
  }

  getStockMovement(form: StockMovementFormGroup): IStockMovement | NewStockMovement {
    return this.convertStockMovementRawValueToStockMovement(form.getRawValue());
  }

  resetForm(form: StockMovementFormGroup, stockMovement: StockMovementFormGroupInput): void {
    const stockMovementRawValue = this.convertStockMovementToStockMovementRawValue({ ...this.getFormDefaults(), ...stockMovement });
    form.reset({
      ...stockMovementRawValue,
      id: { value: stockMovementRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): StockMovementFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      occurredAt: currentTime,
    };
  }

  private convertStockMovementRawValueToStockMovement(
    rawStockMovement: StockMovementFormRawValue | NewStockMovementFormRawValue,
  ): IStockMovement | NewStockMovement {
    return {
      ...rawStockMovement,
      occurredAt: dayjs(rawStockMovement.occurredAt, DATE_TIME_FORMAT),
    };
  }

  private convertStockMovementToStockMovementRawValue(
    stockMovement: IStockMovement | (Partial<NewStockMovement> & StockMovementFormDefaults),
  ): StockMovementFormRawValue | PartialWithRequiredKeyOf<NewStockMovementFormRawValue> {
    return {
      ...stockMovement,
      occurredAt: stockMovement.occurredAt ? stockMovement.occurredAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
