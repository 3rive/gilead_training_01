import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { IMedicine } from '../medicine.model';
import { MedicineService } from '../service/medicine.service';

@Component({
  templateUrl: './medicine-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class MedicineDeleteDialog {
  medicine?: IMedicine;

  protected readonly medicineService = inject(MedicineService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicineService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
