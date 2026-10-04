import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { MovementType } from 'app/entities/enumerations/movement-type.model';
import { StockLotService } from 'app/entities/stock-lot/service/stock-lot.service';
import { IStockLot } from 'app/entities/stock-lot/stock-lot.model';
import { AlertError } from 'app/shared/alert';
import { StockMovementService } from '../service/stock-movement.service';
import { IStockMovement } from '../stock-movement.model';

import { StockMovementFormGroup, StockMovementFormService } from './stock-movement-form.service';

@Component({
  selector: 'mi-stock-movement-update',
  templateUrl: './stock-movement-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class StockMovementUpdate implements OnInit {
  readonly isSaving = signal(false);
  stockMovement: IStockMovement | null = null;
  movementTypeValues = Object.keys(MovementType);

  stockLotsSharedCollection = signal<IStockLot[]>([]);

  protected stockMovementService = inject(StockMovementService);
  protected stockMovementFormService = inject(StockMovementFormService);
  protected stockLotService = inject(StockLotService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StockMovementFormGroup = this.stockMovementFormService.createStockMovementFormGroup();

  compareStockLot = (o1: IStockLot | null, o2: IStockLot | null): boolean => this.stockLotService.compareStockLot(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stockMovement }) => {
      this.stockMovement = stockMovement;
      if (stockMovement) {
        this.updateForm(stockMovement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const stockMovement = this.stockMovementFormService.getStockMovement(this.editForm);
    if (stockMovement.id === null) {
      this.subscribeToSaveResponse(this.stockMovementService.create(stockMovement));
    } else {
      this.subscribeToSaveResponse(this.stockMovementService.update(stockMovement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IStockMovement | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(stockMovement: IStockMovement): void {
    this.stockMovement = stockMovement;
    this.stockMovementFormService.resetForm(this.editForm, stockMovement);

    this.stockLotsSharedCollection.update(stockLots =>
      this.stockLotService.addStockLotToCollectionIfMissing<IStockLot>(stockLots, stockMovement.stockLot),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.stockLotService
      .query()
      .pipe(map((res: HttpResponse<IStockLot[]>) => res.body ?? []))
      .pipe(
        map((stockLots: IStockLot[]) =>
          this.stockLotService.addStockLotToCollectionIfMissing<IStockLot>(stockLots, this.stockMovement?.stockLot),
        ),
      )
      .subscribe((stockLots: IStockLot[]) => this.stockLotsSharedCollection.set(stockLots));
  }
}
