import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IStockMovement } from '../stock-movement.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../stock-movement.test-samples';

import { RestStockMovement, StockMovementService } from './stock-movement.service';

const requireRestSample: RestStockMovement = {
  ...sampleWithRequiredData,
  occurredAt: sampleWithRequiredData.occurredAt?.toJSON(),
};

describe('StockMovement Service', () => {
  let service: StockMovementService;
  let httpMock: HttpTestingController;
  let expectedResult: IStockMovement | IStockMovement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StockMovementService);
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

    it('should create a StockMovement', () => {
      const stockMovement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stockMovement).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StockMovement', () => {
      const stockMovement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stockMovement).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StockMovement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StockMovement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StockMovement', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addStockMovementToCollectionIfMissing', () => {
      it('should add a StockMovement to an empty array', () => {
        const stockMovement: IStockMovement = sampleWithRequiredData;
        expectedResult = service.addStockMovementToCollectionIfMissing([], stockMovement);
        expect(expectedResult).toEqual([stockMovement]);
      });

      it('should not add a StockMovement to an array that contains it', () => {
        const stockMovement: IStockMovement = sampleWithRequiredData;
        const stockMovementCollection: IStockMovement[] = [
          {
            ...stockMovement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStockMovementToCollectionIfMissing(stockMovementCollection, stockMovement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StockMovement to an array that doesn't contain it", () => {
        const stockMovement: IStockMovement = sampleWithRequiredData;
        const stockMovementCollection: IStockMovement[] = [sampleWithPartialData];
        expectedResult = service.addStockMovementToCollectionIfMissing(stockMovementCollection, stockMovement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockMovement);
      });

      it('should add only unique StockMovement to an array', () => {
        const stockMovementArray: IStockMovement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stockMovementCollection: IStockMovement[] = [sampleWithRequiredData];
        expectedResult = service.addStockMovementToCollectionIfMissing(stockMovementCollection, ...stockMovementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stockMovement: IStockMovement = sampleWithRequiredData;
        const stockMovement2: IStockMovement = sampleWithPartialData;
        expectedResult = service.addStockMovementToCollectionIfMissing([], stockMovement, stockMovement2);
        expect(expectedResult).toEqual([stockMovement, stockMovement2]);
      });

      it('should accept null and undefined values', () => {
        const stockMovement: IStockMovement = sampleWithRequiredData;
        expectedResult = service.addStockMovementToCollectionIfMissing([], null, stockMovement, undefined);
        expect(expectedResult).toEqual([stockMovement]);
      });

      it('should return initial array if no StockMovement is added', () => {
        const stockMovementCollection: IStockMovement[] = [sampleWithRequiredData];
        expectedResult = service.addStockMovementToCollectionIfMissing(stockMovementCollection, undefined, null);
        expect(expectedResult).toEqual(stockMovementCollection);
      });
    });

    describe('compareStockMovement', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStockMovement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18917 };
        const entity2 = null;

        const compareResult1 = service.compareStockMovement(entity1, entity2);
        const compareResult2 = service.compareStockMovement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18917 };
        const entity2 = { id: 1833 };

        const compareResult1 = service.compareStockMovement(entity1, entity2);
        const compareResult2 = service.compareStockMovement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 18917 };
        const entity2 = { id: 18917 };

        const compareResult1 = service.compareStockMovement(entity1, entity2);
        const compareResult2 = service.compareStockMovement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
