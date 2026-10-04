import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { StockLotService } from 'app/entities/stock-lot/service/stock-lot.service';
import { IStockLot } from 'app/entities/stock-lot/stock-lot.model';
import { StockMovementService } from '../service/stock-movement.service';
import { IStockMovement } from '../stock-movement.model';

import { StockMovementFormService } from './stock-movement-form.service';
import { StockMovementUpdate } from './stock-movement-update';

describe('StockMovement Management Update Component', () => {
  let comp: StockMovementUpdate;
  let fixture: ComponentFixture<StockMovementUpdate>;
  let activatedRoute: ActivatedRoute;
  let stockMovementFormService: StockMovementFormService;
  let stockMovementService: StockMovementService;
  let stockLotService: StockLotService;

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

    fixture = TestBed.createComponent(StockMovementUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stockMovementFormService = TestBed.inject(StockMovementFormService);
    stockMovementService = TestBed.inject(StockMovementService);
    stockLotService = TestBed.inject(StockLotService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call StockLot query and add missing value', () => {
      const stockMovement: IStockMovement = { id: 1833 };
      const stockLot: IStockLot = { id: 10349 };
      stockMovement.stockLot = stockLot;

      const stockLotCollection: IStockLot[] = [{ id: 10349 }];
      vi.spyOn(stockLotService, 'query').mockReturnValue(of(new HttpResponse({ body: stockLotCollection })));
      const additionalStockLots = [stockLot];
      const expectedCollection: IStockLot[] = [...additionalStockLots, ...stockLotCollection];
      vi.spyOn(stockLotService, 'addStockLotToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ stockMovement });
      comp.ngOnInit();

      expect(stockLotService.query).toHaveBeenCalled();
      expect(stockLotService.addStockLotToCollectionIfMissing).toHaveBeenCalledWith(
        stockLotCollection,
        ...additionalStockLots.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.stockLotsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const stockMovement: IStockMovement = { id: 1833 };
      const stockLot: IStockLot = { id: 10349 };
      stockMovement.stockLot = stockLot;

      activatedRoute.data = of({ stockMovement });
      comp.ngOnInit();

      expect(comp.stockLotsSharedCollection()).toContainEqual(stockLot);
      expect(comp.stockMovement).toEqual(stockMovement);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IStockMovement>();
      const stockMovement = { id: 18917 };
      vi.spyOn(stockMovementFormService, 'getStockMovement').mockReturnValue(stockMovement);
      vi.spyOn(stockMovementService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockMovement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(stockMovement);
      saveSubject.complete();

      // THEN
      expect(stockMovementFormService.getStockMovement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stockMovementService.update).toHaveBeenCalledWith(expect.objectContaining(stockMovement));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IStockMovement>();
      const stockMovement = { id: 18917 };
      vi.spyOn(stockMovementFormService, 'getStockMovement').mockReturnValue({ id: null });
      vi.spyOn(stockMovementService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockMovement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(stockMovement);
      saveSubject.complete();

      // THEN
      expect(stockMovementFormService.getStockMovement).toHaveBeenCalled();
      expect(stockMovementService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IStockMovement>();
      const stockMovement = { id: 18917 };
      vi.spyOn(stockMovementService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockMovement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stockMovementService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareStockLot', () => {
      it('should forward to stockLotService', () => {
        const entity = { id: 10349 };
        const entity2 = { id: 28485 };
        vi.spyOn(stockLotService, 'compareStockLot');
        comp.compareStockLot(entity, entity2);
        expect(stockLotService.compareStockLot).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
