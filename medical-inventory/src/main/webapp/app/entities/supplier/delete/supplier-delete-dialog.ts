import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { SupplierService } from '../service/supplier.service';
import { ISupplier } from '../supplier.model';

@Component({
  templateUrl: './supplier-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class SupplierDeleteDialog {
  supplier?: ISupplier;

  protected readonly supplierService = inject(SupplierService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.supplierService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
