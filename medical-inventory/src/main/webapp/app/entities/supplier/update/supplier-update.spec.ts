import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { SupplierService } from '../service/supplier.service';
import { ISupplier } from '../supplier.model';

import { SupplierFormService } from './supplier-form.service';
import { SupplierUpdate } from './supplier-update';

describe('Supplier Management Update Component', () => {
  let comp: SupplierUpdate;
  let fixture: ComponentFixture<SupplierUpdate>;
  let activatedRoute: ActivatedRoute;
  let supplierFormService: SupplierFormService;
  let supplierService: SupplierService;

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

    fixture = TestBed.createComponent(SupplierUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    supplierFormService = TestBed.inject(SupplierFormService);
    supplierService = TestBed.inject(SupplierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const supplier: ISupplier = { id: 5063 };

      activatedRoute.data = of({ supplier });
      comp.ngOnInit();

      expect(comp.supplier).toEqual(supplier);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ISupplier>();
      const supplier = { id: 28889 };
      vi.spyOn(supplierFormService, 'getSupplier').mockReturnValue(supplier);
      vi.spyOn(supplierService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ supplier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(supplier);
      saveSubject.complete();

      // THEN
      expect(supplierFormService.getSupplier).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(supplierService.update).toHaveBeenCalledWith(expect.objectContaining(supplier));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ISupplier>();
      const supplier = { id: 28889 };
      vi.spyOn(supplierFormService, 'getSupplier').mockReturnValue({ id: null });
      vi.spyOn(supplierService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ supplier: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(supplier);
      saveSubject.complete();

      // THEN
      expect(supplierFormService.getSupplier).toHaveBeenCalled();
      expect(supplierService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ISupplier>();
      const supplier = { id: 28889 };
      vi.spyOn(supplierService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ supplier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(supplierService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
