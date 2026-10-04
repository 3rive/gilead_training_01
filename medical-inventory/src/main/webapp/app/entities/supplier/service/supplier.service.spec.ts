import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ISupplier } from '../supplier.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../supplier.test-samples';

import { SupplierService } from './supplier.service';

const requireRestSample: ISupplier = {
  ...sampleWithRequiredData,
};

describe('Supplier Service', () => {
  let service: SupplierService;
  let httpMock: HttpTestingController;
  let expectedResult: ISupplier | ISupplier[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SupplierService);
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

    it('should create a Supplier', () => {
      const supplier = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(supplier).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Supplier', () => {
      const supplier = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(supplier).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Supplier', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Supplier', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Supplier', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addSupplierToCollectionIfMissing', () => {
      it('should add a Supplier to an empty array', () => {
        const supplier: ISupplier = sampleWithRequiredData;
        expectedResult = service.addSupplierToCollectionIfMissing([], supplier);
        expect(expectedResult).toEqual([supplier]);
      });

      it('should not add a Supplier to an array that contains it', () => {
        const supplier: ISupplier = sampleWithRequiredData;
        const supplierCollection: ISupplier[] = [
          {
            ...supplier,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSupplierToCollectionIfMissing(supplierCollection, supplier);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Supplier to an array that doesn't contain it", () => {
        const supplier: ISupplier = sampleWithRequiredData;
        const supplierCollection: ISupplier[] = [sampleWithPartialData];
        expectedResult = service.addSupplierToCollectionIfMissing(supplierCollection, supplier);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(supplier);
      });

      it('should add only unique Supplier to an array', () => {
        const supplierArray: ISupplier[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const supplierCollection: ISupplier[] = [sampleWithRequiredData];
        expectedResult = service.addSupplierToCollectionIfMissing(supplierCollection, ...supplierArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const supplier: ISupplier = sampleWithRequiredData;
        const supplier2: ISupplier = sampleWithPartialData;
        expectedResult = service.addSupplierToCollectionIfMissing([], supplier, supplier2);
        expect(expectedResult).toEqual([supplier, supplier2]);
      });

      it('should accept null and undefined values', () => {
        const supplier: ISupplier = sampleWithRequiredData;
        expectedResult = service.addSupplierToCollectionIfMissing([], null, supplier, undefined);
        expect(expectedResult).toEqual([supplier]);
      });

      it('should return initial array if no Supplier is added', () => {
        const supplierCollection: ISupplier[] = [sampleWithRequiredData];
        expectedResult = service.addSupplierToCollectionIfMissing(supplierCollection, undefined, null);
        expect(expectedResult).toEqual(supplierCollection);
      });
    });

    describe('compareSupplier', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSupplier(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28889 };
        const entity2 = null;

        const compareResult1 = service.compareSupplier(entity1, entity2);
        const compareResult2 = service.compareSupplier(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28889 };
        const entity2 = { id: 5063 };

        const compareResult1 = service.compareSupplier(entity1, entity2);
        const compareResult2 = service.compareSupplier(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 28889 };
        const entity2 = { id: 28889 };

        const compareResult1 = service.compareSupplier(entity1, entity2);
        const compareResult2 = service.compareSupplier(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
