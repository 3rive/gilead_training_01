import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { IMedicine } from '../medicine.model';
import { MedicineService } from '../service/medicine.service';

import { MedicineFormService } from './medicine-form.service';
import { MedicineUpdate } from './medicine-update';

describe('Medicine Management Update Component', () => {
  let comp: MedicineUpdate;
  let fixture: ComponentFixture<MedicineUpdate>;
  let activatedRoute: ActivatedRoute;
  let medicineFormService: MedicineFormService;
  let medicineService: MedicineService;
  let categoryService: CategoryService;
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

    fixture = TestBed.createComponent(MedicineUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicineFormService = TestBed.inject(MedicineFormService);
    medicineService = TestBed.inject(MedicineService);
    categoryService = TestBed.inject(CategoryService);
    supplierService = TestBed.inject(SupplierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Category query and add missing value', () => {
      const medicine: IMedicine = { id: 21119 };
      const category: ICategory = { id: 6752 };
      medicine.category = category;

      const categoryCollection: ICategory[] = [{ id: 6752 }];
      vi.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [category];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      vi.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medicine });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        categoryCollection,
        ...additionalCategories.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.categoriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Supplier query and add missing value', () => {
      const medicine: IMedicine = { id: 21119 };
      const supplier: ISupplier = { id: 28889 };
      medicine.supplier = supplier;

      const supplierCollection: ISupplier[] = [{ id: 28889 }];
      vi.spyOn(supplierService, 'query').mockReturnValue(of(new HttpResponse({ body: supplierCollection })));
      const additionalSuppliers = [supplier];
      const expectedCollection: ISupplier[] = [...additionalSuppliers, ...supplierCollection];
      vi.spyOn(supplierService, 'addSupplierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medicine });
      comp.ngOnInit();

      expect(supplierService.query).toHaveBeenCalled();
      expect(supplierService.addSupplierToCollectionIfMissing).toHaveBeenCalledWith(
        supplierCollection,
        ...additionalSuppliers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.suppliersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const medicine: IMedicine = { id: 21119 };
      const category: ICategory = { id: 6752 };
      medicine.category = category;
      const supplier: ISupplier = { id: 28889 };
      medicine.supplier = supplier;

      activatedRoute.data = of({ medicine });
      comp.ngOnInit();

      expect(comp.categoriesSharedCollection()).toContainEqual(category);
      expect(comp.suppliersSharedCollection()).toContainEqual(supplier);
      expect(comp.medicine).toEqual(medicine);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IMedicine>();
      const medicine = { id: 19901 };
      vi.spyOn(medicineFormService, 'getMedicine').mockReturnValue(medicine);
      vi.spyOn(medicineService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(medicine);
      saveSubject.complete();

      // THEN
      expect(medicineFormService.getMedicine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicineService.update).toHaveBeenCalledWith(expect.objectContaining(medicine));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IMedicine>();
      const medicine = { id: 19901 };
      vi.spyOn(medicineFormService, 'getMedicine').mockReturnValue({ id: null });
      vi.spyOn(medicineService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(medicine);
      saveSubject.complete();

      // THEN
      expect(medicineFormService.getMedicine).toHaveBeenCalled();
      expect(medicineService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IMedicine>();
      const medicine = { id: 19901 };
      vi.spyOn(medicineService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicineService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCategory', () => {
      it('should forward to categoryService', () => {
        const entity = { id: 6752 };
        const entity2 = { id: 4374 };
        vi.spyOn(categoryService, 'compareCategory');
        comp.compareCategory(entity, entity2);
        expect(categoryService.compareCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSupplier', () => {
      it('should forward to supplierService', () => {
        const entity = { id: 28889 };
        const entity2 = { id: 5063 };
        vi.spyOn(supplierService, 'compareSupplier');
        comp.compareSupplier(entity, entity2);
        expect(supplierService.compareSupplier).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
