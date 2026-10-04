import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../storage-location.test-samples';

import { StorageLocationFormService } from './storage-location-form.service';

describe('StorageLocation Form Service', () => {
  let service: StorageLocationFormService;

  beforeEach(() => {
    service = TestBed.inject(StorageLocationFormService);
  });

  describe('Service methods', () => {
    describe('createStorageLocationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStorageLocationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            building: expect.any(Object),
            temperatureControlled: expect.any(Object),
          }),
        );
      });

      it('passing IStorageLocation should create a new form with FormGroup', () => {
        const formGroup = service.createStorageLocationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            building: expect.any(Object),
            temperatureControlled: expect.any(Object),
          }),
        );
      });
    });

    describe('getStorageLocation', () => {
      it('should return NewStorageLocation for default StorageLocation initial value', () => {
        const formGroup = service.createStorageLocationFormGroup(sampleWithNewData);

        const storageLocation = service.getStorageLocation(formGroup);

        expect(storageLocation).toMatchObject(sampleWithNewData);
      });

      it('should return NewStorageLocation for empty StorageLocation initial value', () => {
        const formGroup = service.createStorageLocationFormGroup();

        const storageLocation = service.getStorageLocation(formGroup);

        expect(storageLocation).toMatchObject({});
      });

      it('should return IStorageLocation', () => {
        const formGroup = service.createStorageLocationFormGroup(sampleWithRequiredData);

        const storageLocation = service.getStorageLocation(formGroup);

        expect(storageLocation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStorageLocation should not enable id FormControl', () => {
        const formGroup = service.createStorageLocationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStorageLocation should disable id FormControl', () => {
        const formGroup = service.createStorageLocationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
