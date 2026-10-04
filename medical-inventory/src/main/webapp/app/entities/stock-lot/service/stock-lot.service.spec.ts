import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { IStockLot } from '../stock-lot.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../stock-lot.test-samples';

import { RestStockLot, StockLotService } from './stock-lot.service';

const requireRestSample: RestStockLot = {
  ...sampleWithRequiredData,
  expiryDate: sampleWithRequiredData.expiryDate?.format(DATE_FORMAT),
  receivedDate: sampleWithRequiredData.receivedDate?.format(DATE_FORMAT),
};

describe('StockLot Service', () => {
  let service: StockLotService;
  let httpMock: HttpTestingController;
  let expectedResult: IStockLot | IStockLot[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StockLotService);
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

    it('should create a StockLot', () => {
      const stockLot = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stockLot).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StockLot', () => {
      const stockLot = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stockLot).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StockLot', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StockLot', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StockLot', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addStockLotToCollectionIfMissing', () => {
      it('should add a StockLot to an empty array', () => {
        const stockLot: IStockLot = sampleWithRequiredData;
        expectedResult = service.addStockLotToCollectionIfMissing([], stockLot);
        expect(expectedResult).toEqual([stockLot]);
      });

      it('should not add a StockLot to an array that contains it', () => {
        const stockLot: IStockLot = sampleWithRequiredData;
        const stockLotCollection: IStockLot[] = [
          {
            ...stockLot,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStockLotToCollectionIfMissing(stockLotCollection, stockLot);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StockLot to an array that doesn't contain it", () => {
        const stockLot: IStockLot = sampleWithRequiredData;
        const stockLotCollection: IStockLot[] = [sampleWithPartialData];
        expectedResult = service.addStockLotToCollectionIfMissing(stockLotCollection, stockLot);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockLot);
      });

      it('should add only unique StockLot to an array', () => {
        const stockLotArray: IStockLot[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stockLotCollection: IStockLot[] = [sampleWithRequiredData];
        expectedResult = service.addStockLotToCollectionIfMissing(stockLotCollection, ...stockLotArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stockLot: IStockLot = sampleWithRequiredData;
        const stockLot2: IStockLot = sampleWithPartialData;
        expectedResult = service.addStockLotToCollectionIfMissing([], stockLot, stockLot2);
        expect(expectedResult).toEqual([stockLot, stockLot2]);
      });

      it('should accept null and undefined values', () => {
        const stockLot: IStockLot = sampleWithRequiredData;
        expectedResult = service.addStockLotToCollectionIfMissing([], null, stockLot, undefined);
        expect(expectedResult).toEqual([stockLot]);
      });

      it('should return initial array if no StockLot is added', () => {
        const stockLotCollection: IStockLot[] = [sampleWithRequiredData];
        expectedResult = service.addStockLotToCollectionIfMissing(stockLotCollection, undefined, null);
        expect(expectedResult).toEqual(stockLotCollection);
      });
    });

    describe('compareStockLot', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStockLot(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 10349 };
        const entity2 = null;

        const compareResult1 = service.compareStockLot(entity1, entity2);
        const compareResult2 = service.compareStockLot(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 10349 };
        const entity2 = { id: 28485 };

        const compareResult1 = service.compareStockLot(entity1, entity2);
        const compareResult2 = service.compareStockLot(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 10349 };
        const entity2 = { id: 10349 };

        const compareResult1 = service.compareStockLot(entity1, entity2);
        const compareResult2 = service.compareStockLot(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
