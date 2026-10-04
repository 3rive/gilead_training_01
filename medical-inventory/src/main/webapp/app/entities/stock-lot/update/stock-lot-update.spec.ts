import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IMedicine } from 'app/entities/medicine/medicine.model';
import { MedicineService } from 'app/entities/medicine/service/medicine.service';
import { StorageLocationService } from 'app/entities/storage-location/service/storage-location.service';
import { IStorageLocation } from 'app/entities/storage-location/storage-location.model';
import { StockLotService } from '../service/stock-lot.service';
import { IStockLot } from '../stock-lot.model';

import { StockLotFormService } from './stock-lot-form.service';
import { StockLotUpdate } from './stock-lot-update';

describe('StockLot Management Update Component', () => {
  let comp: StockLotUpdate;
  let fixture: ComponentFixture<StockLotUpdate>;
  let activatedRoute: ActivatedRoute;
  let stockLotFormService: StockLotFormService;
  let stockLotService: StockLotService;
  let medicineService: MedicineService;
  let storageLocationService: StorageLocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(StockLotUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stockLotFormService = TestBed.inject(StockLotFormService);
    stockLotService = TestBed.inject(StockLotService);
    medicineService = TestBed.inject(MedicineService);
    storageLocationService = TestBed.inject(StorageLocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Medicine query and add missing value', () => {
      const stockLot: IStockLot = { id: 28485 };
      const medicine: IMedicine = { id: 19901 };
      stockLot.medicine = medicine;

      const medicineCollection: IMedicine[] = [{ id: 19901 }];
      vi.spyOn(medicineService, 'query').mockReturnValue(of(new HttpResponse({ body: medicineCollection })));
      const additionalMedicines = [medicine];
      const expectedCollection: IMedicine[] = [...additionalMedicines, ...medicineCollection];
      vi.spyOn(medicineService, 'addMedicineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ stockLot });
      comp.ngOnInit();

      expect(medicineService.query).toHaveBeenCalled();
      expect(medicineService.addMedicineToCollectionIfMissing).toHaveBeenCalledWith(
        medicineCollection,
        ...additionalMedicines.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.medicinesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call StorageLocation query and add missing value', () => {
      const stockLot: IStockLot = { id: 28485 };
      const storageLocation: IStorageLocation = { id: 32745 };
      stockLot.storageLocation = storageLocation;

      const storageLocationCollection: IStorageLocation[] = [{ id: 32745 }];
      vi.spyOn(storageLocationService, 'query').mockReturnValue(of(new HttpResponse({ body: storageLocationCollection })));
      const additionalStorageLocations = [storageLocation];
      const expectedCollection: IStorageLocation[] = [...additionalStorageLocations, ...storageLocationCollection];
      vi.spyOn(storageLocationService, 'addStorageLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ stockLot });
      comp.ngOnInit();

      expect(storageLocationService.query).toHaveBeenCalled();
      expect(storageLocationService.addStorageLocationToCollectionIfMissing).toHaveBeenCalledWith(
        storageLocationCollection,
        ...additionalStorageLocations.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.storageLocationsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const stockLot: IStockLot = { id: 28485 };
      const medicine: IMedicine = { id: 19901 };
      stockLot.medicine = medicine;
      const storageLocation: IStorageLocation = { id: 32745 };
      stockLot.storageLocation = storageLocation;

      activatedRoute.data = of({ stockLot });
      comp.ngOnInit();

      expect(comp.medicinesSharedCollection()).toContainEqual(medicine);
      expect(comp.storageLocationsSharedCollection()).toContainEqual(storageLocation);
      expect(comp.stockLot).toEqual(stockLot);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IStockLot>();
      const stockLot = { id: 10349 };
      vi.spyOn(stockLotFormService, 'getStockLot').mockReturnValue(stockLot);
      vi.spyOn(stockLotService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockLot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(stockLot);
      saveSubject.complete();

      // THEN
      expect(stockLotFormService.getStockLot).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stockLotService.update).toHaveBeenCalledWith(expect.objectContaining(stockLot));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IStockLot>();
      const stockLot = { id: 10349 };
      vi.spyOn(stockLotFormService, 'getStockLot').mockReturnValue({ id: null });
      vi.spyOn(stockLotService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockLot: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(stockLot);
      saveSubject.complete();

      // THEN
      expect(stockLotFormService.getStockLot).toHaveBeenCalled();
      expect(stockLotService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IStockLot>();
      const stockLot = { id: 10349 };
      vi.spyOn(stockLotService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockLot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stockLotService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMedicine', () => {
      it('should forward to medicineService', () => {
        const entity = { id: 19901 };
        const entity2 = { id: 21119 };
        vi.spyOn(medicineService, 'compareMedicine');
        comp.compareMedicine(entity, entity2);
        expect(medicineService.compareMedicine).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStorageLocation', () => {
      it('should forward to storageLocationService', () => {
        const entity = { id: 32745 };
        const entity2 = { id: 5420 };
        vi.spyOn(storageLocationService, 'compareStorageLocation');
        comp.compareStorageLocation(entity, entity2);
        expect(storageLocationService.compareStorageLocation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
