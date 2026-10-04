import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { StorageLocationService } from '../service/storage-location.service';
import { IStorageLocation } from '../storage-location.model';

@Component({
  templateUrl: './storage-location-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class StorageLocationDeleteDialog {
  storageLocation?: IStorageLocation;

  protected readonly storageLocationService = inject(StorageLocationService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.storageLocationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
