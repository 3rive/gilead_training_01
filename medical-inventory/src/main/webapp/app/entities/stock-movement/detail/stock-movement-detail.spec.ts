import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { of } from 'rxjs';

import { StockMovementDetail } from './stock-movement-detail';

describe('StockMovement Management Detail Component', () => {
  let comp: StockMovementDetail;
  let fixture: ComponentFixture<StockMovementDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./stock-movement-detail').then(m => m.StockMovementDetail),
              resolve: { stockMovement: () => of({ id: 18917 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StockMovementDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load stockMovement on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StockMovementDetail);

      // THEN
      expect(instance.stockMovement()).toEqual(expect.objectContaining({ id: 18917 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vi.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
