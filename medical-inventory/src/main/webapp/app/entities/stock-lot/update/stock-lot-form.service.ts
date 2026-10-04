import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IStockLot, NewStockLot } from '../stock-lot.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStockLot for edit and NewStockLotFormGroupInput for create.
 */
type StockLotFormGroupInput = IStockLot | PartialWithRequiredKeyOf<NewStockLot>;

type StockLotFormDefaults = Pick<NewStockLot, 'id'>;

type StockLotFormGroupContent = {
  id: FormControl<IStockLot['id'] | NewStockLot['id']>;
  batchNumber: FormControl<IStockLot['batchNumber']>;
  expiryDate: FormControl<IStockLot['expiryDate']>;
  quantityOnHand: FormControl<IStockLot['quantityOnHand']>;
  receivedDate: FormControl<IStockLot['receivedDate']>;
  medicine: FormControl<IStockLot['medicine']>;
  storageLocation: FormControl<IStockLot['storageLocation']>;
};

export type StockLotFormGroup = FormGroup<StockLotFormGroupContent>;

@Service()
export class StockLotFormService {
  createStockLotFormGroup(stockLot?: StockLotFormGroupInput): StockLotFormGroup {
    const stockLotRawValue = {
      ...this.getFormDefaults(),
      ...(stockLot ?? { id: null }),
    };

    return new FormGroup<StockLotFormGroupContent>({
      id: new FormControl(
        { value: stockLotRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      batchNumber: new FormControl(stockLotRawValue.batchNumber, {
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(40)],
      }),
      expiryDate: new FormControl(stockLotRawValue.expiryDate, {
        validators: [Validators.required],
      }),
      quantityOnHand: new FormControl(stockLotRawValue.quantityOnHand, {
        validators: [Validators.required, Validators.min(0)],
      }),
      receivedDate: new FormControl(stockLotRawValue.receivedDate, {
        validators: [Validators.required],
      }),
      medicine: new FormControl(stockLotRawValue.medicine, {
        validators: [Validators.required],
      }),
      storageLocation: new FormControl(stockLotRawValue.storageLocation, {
        validators: [Validators.required],
      }),
    });
  }

  getStockLot(form: StockLotFormGroup): IStockLot | NewStockLot {
    return form.getRawValue();
  }

  resetForm(form: StockLotFormGroup, stockLot: StockLotFormGroupInput): void {
    const stockLotRawValue = { ...this.getFormDefaults(), ...stockLot };
    form.reset({
      ...stockLotRawValue,
      id: { value: stockLotRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): StockLotFormDefaults {
    return {
      id: null,
    };
  }
}
