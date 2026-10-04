import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IMedicine } from '../medicine.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../medicine.test-samples';

import { MedicineService } from './medicine.service';

const requireRestSample: IMedicine = {
  ...sampleWithRequiredData,
};

describe('Medicine Service', () => {
  let service: MedicineService;
  let httpMock: HttpTestingController;
  let expectedResult: IMedicine | IMedicine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MedicineService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Medicine', () => {
      const medicine = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(medicine).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Medicine', () => {
      const medicine = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(medicine).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Medicine', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Medicine', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Medicine', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addMedicineToCollectionIfMissing', () => {
      it('should add a Medicine to an empty array', () => {
        const medicine: IMedicine = sampleWithRequiredData;
        expectedResult = service.addMedicineToCollectionIfMissing([], medicine);
        expect(expectedResult).toEqual([medicine]);
      });

      it('should not add a Medicine to an array that contains it', () => {
        const medicine: IMedicine = sampleWithRequiredData;
        const medicineCollection: IMedicine[] = [
          {
            ...medicine,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMedicineToCollectionIfMissing(medicineCollection, medicine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Medicine to an array that doesn't contain it", () => {
        const medicine: IMedicine = sampleWithRequiredData;
        const medicineCollection: IMedicine[] = [sampleWithPartialData];
        expectedResult = service.addMedicineToCollectionIfMissing(medicineCollection, medicine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicine);
      });

      it('should add only unique Medicine to an array', () => {
        const medicineArray: IMedicine[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const medicineCollection: IMedicine[] = [sampleWithRequiredData];
        expectedResult = service.addMedicineToCollectionIfMissing(medicineCollection, ...medicineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const medicine: IMedicine = sampleWithRequiredData;
        const medicine2: IMedicine = sampleWithPartialData;
        expectedResult = service.addMedicineToCollectionIfMissing([], medicine, medicine2);
        expect(expectedResult).toEqual([medicine, medicine2]);
      });

      it('should accept null and undefined values', () => {
        const medicine: IMedicine = sampleWithRequiredData;
        expectedResult = service.addMedicineToCollectionIfMissing([], null, medicine, undefined);
        expect(expectedResult).toEqual([medicine]);
      });

      it('should return initial array if no Medicine is added', () => {
        const medicineCollection: IMedicine[] = [sampleWithRequiredData];
        expectedResult = service.addMedicineToCollectionIfMissing(medicineCollection, undefined, null);
        expect(expectedResult).toEqual(medicineCollection);
      });
    });

    describe('compareMedicine', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMedicine(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 19901 };
        const entity2 = null;

        const compareResult1 = service.compareMedicine(entity1, entity2);
        const compareResult2 = service.compareMedicine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 19901 };
        const entity2 = { id: 21119 };

        const compareResult1 = service.compareMedicine(entity1, entity2);
        const compareResult2 = service.compareMedicine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 19901 };
        const entity2 = { id: 19901 };

        const compareResult1 = service.compareMedicine(entity1, entity2);
        const compareResult2 = service.compareMedicine(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
