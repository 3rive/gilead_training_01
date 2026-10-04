import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../medicine.test-samples';

import { MedicineFormService } from './medicine-form.service';

describe('Medicine Form Service', () => {
  let service: MedicineFormService;

  beforeEach(() => {
    service = TestBed.inject(MedicineFormService);
  });

  describe('Service methods', () => {
    describe('createMedicineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMedicineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            sku: expect.any(Object),
            genericName: expect.any(Object),
            dosageForm: expect.any(Object),
            strength: expect.any(Object),
            unitPrice: expect.any(Object),
            reorderLevel: expect.any(Object),
            controlledSubstance: expect.any(Object),
            description: expect.any(Object),
            category: expect.any(Object),
            supplier: expect.any(Object),
          }),
        );
      });

      it('passing IMedicine should create a new form with FormGroup', () => {
        const formGroup = service.createMedicineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            sku: expect.any(Object),
            genericName: expect.any(Object),
            dosageForm: expect.any(Object),
            strength: expect.any(Object),
            unitPrice: expect.any(Object),
            reorderLevel: expect.any(Object),
            controlledSubstance: expect.any(Object),
            description: expect.any(Object),
            category: expect.any(Object),
            supplier: expect.any(Object),
          }),
        );
      });
    });

    describe('getMedicine', () => {
      it('should return NewMedicine for default Medicine initial value', () => {
        const formGroup = service.createMedicineFormGroup(sampleWithNewData);

        const medicine = service.getMedicine(formGroup);

        expect(medicine).toMatchObject(sampleWithNewData);
      });

      it('should return NewMedicine for empty Medicine initial value', () => {
        const formGroup = service.createMedicineFormGroup();

        const medicine = service.getMedicine(formGroup);

        expect(medicine).toMatchObject({});
      });

      it('should return IMedicine', () => {
        const formGroup = service.createMedicineFormGroup(sampleWithRequiredData);

        const medicine = service.getMedicine(formGroup);

        expect(medicine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMedicine should not enable id FormControl', () => {
        const formGroup = service.createMedicineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMedicine should disable id FormControl', () => {
        const formGroup = service.createMedicineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
