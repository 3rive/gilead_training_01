import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../stock-lot.test-samples';

import { StockLotFormService } from './stock-lot-form.service';

describe('StockLot Form Service', () => {
  let service: StockLotFormService;

  beforeEach(() => {
    service = TestBed.inject(StockLotFormService);
  });

  describe('Service methods', () => {
    describe('createStockLotFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStockLotFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            batchNumber: expect.any(Object),
            expiryDate: expect.any(Object),
            quantityOnHand: expect.any(Object),
            receivedDate: expect.any(Object),
            medicine: expect.any(Object),
            storageLocation: expect.any(Object),
          }),
        );
      });

      it('passing IStockLot should create a new form with FormGroup', () => {
        const formGroup = service.createStockLotFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            batchNumber: expect.any(Object),
            expiryDate: expect.any(Object),
            quantityOnHand: expect.any(Object),
            receivedDate: expect.any(Object),
            medicine: expect.any(Object),
            storageLocation: expect.any(Object),
          }),
        );
      });
    });

    describe('getStockLot', () => {
      it('should return NewStockLot for default StockLot initial value', () => {
        const formGroup = service.createStockLotFormGroup(sampleWithNewData);

        const stockLot = service.getStockLot(formGroup);

        expect(stockLot).toMatchObject(sampleWithNewData);
      });

      it('should return NewStockLot for empty StockLot initial value', () => {
        const formGroup = service.createStockLotFormGroup();

        const stockLot = service.getStockLot(formGroup);

        expect(stockLot).toMatchObject({});
      });

      it('should return IStockLot', () => {
        const formGroup = service.createStockLotFormGroup(sampleWithRequiredData);

        const stockLot = service.getStockLot(formGroup);

        expect(stockLot).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStockLot should not enable id FormControl', () => {
        const formGroup = service.createStockLotFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStockLot should disable id FormControl', () => {
        const formGroup = service.createStockLotFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
