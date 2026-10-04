import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { StorageLocationService } from '../service/storage-location.service';
import { IStorageLocation } from '../storage-location.model';

import { StorageLocationFormService } from './storage-location-form.service';
import { StorageLocationUpdate } from './storage-location-update';

describe('StorageLocation Management Update Component', () => {
  let comp: StorageLocationUpdate;
  let fixture: ComponentFixture<StorageLocationUpdate>;
  let activatedRoute: ActivatedRoute;
  let storageLocationFormService: StorageLocationFormService;
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

    fixture = TestBed.createComponent(StorageLocationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    storageLocationFormService = TestBed.inject(StorageLocationFormService);
    storageLocationService = TestBed.inject(StorageLocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const storageLocation: IStorageLocation = { id: 5420 };

      activatedRoute.data = of({ storageLocation });
      comp.ngOnInit();

      expect(comp.storageLocation).toEqual(storageLocation);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IStorageLocation>();
      const storageLocation = { id: 32745 };
      vi.spyOn(storageLocationFormService, 'getStorageLocation').mockReturnValue(storageLocation);
      vi.spyOn(storageLocationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ storageLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(storageLocation);
      saveSubject.complete();

      // THEN
      expect(storageLocationFormService.getStorageLocation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(storageLocationService.update).toHaveBeenCalledWith(expect.objectContaining(storageLocation));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IStorageLocation>();
      const storageLocation = { id: 32745 };
      vi.spyOn(storageLocationFormService, 'getStorageLocation').mockReturnValue({ id: null });
      vi.spyOn(storageLocationService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ storageLocation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(storageLocation);
      saveSubject.complete();

      // THEN
      expect(storageLocationFormService.getStorageLocation).toHaveBeenCalled();
      expect(storageLocationService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IStorageLocation>();
      const storageLocation = { id: 32745 };
      vi.spyOn(storageLocationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ storageLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(storageLocationService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
