import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { StorageLocationService } from '../service/storage-location.service';
import { IStorageLocation } from '../storage-location.model';

import { StorageLocationFormGroup, StorageLocationFormService } from './storage-location-form.service';

@Component({
  selector: 'mi-storage-location-update',
  templateUrl: './storage-location-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class StorageLocationUpdate implements OnInit {
  readonly isSaving = signal(false);
  storageLocation: IStorageLocation | null = null;

  protected storageLocationService = inject(StorageLocationService);
  protected storageLocationFormService = inject(StorageLocationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StorageLocationFormGroup = this.storageLocationFormService.createStorageLocationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ storageLocation }) => {
      this.storageLocation = storageLocation;
      if (storageLocation) {
        this.updateForm(storageLocation);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const storageLocation = this.storageLocationFormService.getStorageLocation(this.editForm);
    if (storageLocation.id === null) {
      this.subscribeToSaveResponse(this.storageLocationService.create(storageLocation));
    } else {
      this.subscribeToSaveResponse(this.storageLocationService.update(storageLocation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IStorageLocation | null>): void {
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

  protected updateForm(storageLocation: IStorageLocation): void {
    this.storageLocation = storageLocation;
    this.storageLocationFormService.resetForm(this.editForm, storageLocation);
  }
}
