import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../stock-movement.test-samples';

import { StockMovementFormService } from './stock-movement-form.service';

describe('StockMovement Form Service', () => {
  let service: StockMovementFormService;

  beforeEach(() => {
    service = TestBed.inject(StockMovementFormService);
  });

  describe('Service methods', () => {
    describe('createStockMovementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStockMovementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            movementType: expect.any(Object),
            quantity: expect.any(Object),
            occurredAt: expect.any(Object),
            reason: expect.any(Object),
            referenceNumber: expect.any(Object),
            stockLot: expect.any(Object),
          }),
        );
      });

      it('passing IStockMovement should create a new form with FormGroup', () => {
        const formGroup = service.createStockMovementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            movementType: expect.any(Object),
            quantity: expect.any(Object),
            occurredAt: expect.any(Object),
            reason: expect.any(Object),
            referenceNumber: expect.any(Object),
            stockLot: expect.any(Object),
          }),
        );
      });
    });

    describe('getStockMovement', () => {
      it('should return NewStockMovement for default StockMovement initial value', () => {
        const formGroup = service.createStockMovementFormGroup(sampleWithNewData);

        const stockMovement = service.getStockMovement(formGroup);

        expect(stockMovement).toMatchObject(sampleWithNewData);
      });

      it('should return NewStockMovement for empty StockMovement initial value', () => {
        const formGroup = service.createStockMovementFormGroup();

        const stockMovement = service.getStockMovement(formGroup);

        expect(stockMovement).toMatchObject({});
      });

      it('should return IStockMovement', () => {
        const formGroup = service.createStockMovementFormGroup(sampleWithRequiredData);

        const stockMovement = service.getStockMovement(formGroup);

        expect(stockMovement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStockMovement should not enable id FormControl', () => {
        const formGroup = service.createStockMovementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStockMovement should disable id FormControl', () => {
        const formGroup = service.createStockMovementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
