import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize, map } from 'rxjs';

import { IMedicine } from 'app/entities/medicine/medicine.model';
import { MedicineService } from 'app/entities/medicine/service/medicine.service';
import { StorageLocationService } from 'app/entities/storage-location/service/storage-location.service';
import { IStorageLocation } from 'app/entities/storage-location/storage-location.model';
import { AlertError } from 'app/shared/alert';
import { StockLotService } from '../service/stock-lot.service';
import { IStockLot } from '../stock-lot.model';

import { StockLotFormGroup, StockLotFormService } from './stock-lot-form.service';

@Component({
  selector: 'mi-stock-lot-update',
  templateUrl: './stock-lot-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class StockLotUpdate implements OnInit {
  readonly isSaving = signal(false);
  stockLot: IStockLot | null = null;

  medicinesSharedCollection = signal<IMedicine[]>([]);
  storageLocationsSharedCollection = signal<IStorageLocation[]>([]);

  protected stockLotService = inject(StockLotService);
  protected stockLotFormService = inject(StockLotFormService);
  protected medicineService = inject(MedicineService);
  protected storageLocationService = inject(StorageLocationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StockLotFormGroup = this.stockLotFormService.createStockLotFormGroup();

  compareMedicine = (o1: IMedicine | null, o2: IMedicine | null): boolean => this.medicineService.compareMedicine(o1, o2);

  compareStorageLocation = (o1: IStorageLocation | null, o2: IStorageLocation | null): boolean =>
    this.storageLocationService.compareStorageLocation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stockLot }) => {
      this.stockLot = stockLot;
      if (stockLot) {
        this.updateForm(stockLot);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const stockLot = this.stockLotFormService.getStockLot(this.editForm);
    if (stockLot.id === null) {
      this.subscribeToSaveResponse(this.stockLotService.create(stockLot));
    } else {
      this.subscribeToSaveResponse(this.stockLotService.update(stockLot));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IStockLot | null>): void {
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

  protected updateForm(stockLot: IStockLot): void {
    this.stockLot = stockLot;
    this.stockLotFormService.resetForm(this.editForm, stockLot);

    this.medicinesSharedCollection.update(medicines =>
      this.medicineService.addMedicineToCollectionIfMissing<IMedicine>(medicines, stockLot.medicine),
    );
    this.storageLocationsSharedCollection.update(storageLocations =>
      this.storageLocationService.addStorageLocationToCollectionIfMissing<IStorageLocation>(storageLocations, stockLot.storageLocation),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicineService
      .query()
      .pipe(map((res: HttpResponse<IMedicine[]>) => res.body ?? []))
      .pipe(
        map((medicines: IMedicine[]) =>
          this.medicineService.addMedicineToCollectionIfMissing<IMedicine>(medicines, this.stockLot?.medicine),
        ),
      )
      .subscribe((medicines: IMedicine[]) => this.medicinesSharedCollection.set(medicines));

    this.storageLocationService
      .query()
      .pipe(map((res: HttpResponse<IStorageLocation[]>) => res.body ?? []))
      .pipe(
        map((storageLocations: IStorageLocation[]) =>
          this.storageLocationService.addStorageLocationToCollectionIfMissing<IStorageLocation>(
            storageLocations,
            this.stockLot?.storageLocation,
          ),
        ),
      )
      .subscribe((storageLocations: IStorageLocation[]) => this.storageLocationsSharedCollection.set(storageLocations));
  }
}
