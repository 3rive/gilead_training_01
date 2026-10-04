import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IStorageLocation } from '../storage-location.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../storage-location.test-samples';

import { StorageLocationService } from './storage-location.service';

const requireRestSample: IStorageLocation = {
  ...sampleWithRequiredData,
};

describe('StorageLocation Service', () => {
  let service: StorageLocationService;
  let httpMock: HttpTestingController;
  let expectedResult: IStorageLocation | IStorageLocation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StorageLocationService);
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

    it('should create a StorageLocation', () => {
      const storageLocation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(storageLocation).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StorageLocation', () => {
      const storageLocation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(storageLocation).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StorageLocation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StorageLocation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StorageLocation', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addStorageLocationToCollectionIfMissing', () => {
      it('should add a StorageLocation to an empty array', () => {
        const storageLocation: IStorageLocation = sampleWithRequiredData;
        expectedResult = service.addStorageLocationToCollectionIfMissing([], storageLocation);
        expect(expectedResult).toEqual([storageLocation]);
      });

      it('should not add a StorageLocation to an array that contains it', () => {
        const storageLocation: IStorageLocation = sampleWithRequiredData;
        const storageLocationCollection: IStorageLocation[] = [
          {
            ...storageLocation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStorageLocationToCollectionIfMissing(storageLocationCollection, storageLocation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StorageLocation to an array that doesn't contain it", () => {
        const storageLocation: IStorageLocation = sampleWithRequiredData;
        const storageLocationCollection: IStorageLocation[] = [sampleWithPartialData];
        expectedResult = service.addStorageLocationToCollectionIfMissing(storageLocationCollection, storageLocation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(storageLocation);
      });

      it('should add only unique StorageLocation to an array', () => {
        const storageLocationArray: IStorageLocation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const storageLocationCollection: IStorageLocation[] = [sampleWithRequiredData];
        expectedResult = service.addStorageLocationToCollectionIfMissing(storageLocationCollection, ...storageLocationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const storageLocation: IStorageLocation = sampleWithRequiredData;
        const storageLocation2: IStorageLocation = sampleWithPartialData;
        expectedResult = service.addStorageLocationToCollectionIfMissing([], storageLocation, storageLocation2);
        expect(expectedResult).toEqual([storageLocation, storageLocation2]);
      });

      it('should accept null and undefined values', () => {
        const storageLocation: IStorageLocation = sampleWithRequiredData;
        expectedResult = service.addStorageLocationToCollectionIfMissing([], null, storageLocation, undefined);
        expect(expectedResult).toEqual([storageLocation]);
      });

      it('should return initial array if no StorageLocation is added', () => {
        const storageLocationCollection: IStorageLocation[] = [sampleWithRequiredData];
        expectedResult = service.addStorageLocationToCollectionIfMissing(storageLocationCollection, undefined, null);
        expect(expectedResult).toEqual(storageLocationCollection);
      });
    });

    describe('compareStorageLocation', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStorageLocation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 32745 };
        const entity2 = null;

        const compareResult1 = service.compareStorageLocation(entity1, entity2);
        const compareResult2 = service.compareStorageLocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 32745 };
        const entity2 = { id: 5420 };

        const compareResult1 = service.compareStorageLocation(entity1, entity2);
        const compareResult2 = service.compareStorageLocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 32745 };
        const entity2 = { id: 32745 };

        const compareResult1 = service.compareStorageLocation(entity1, entity2);
        const compareResult2 = service.compareStorageLocation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
