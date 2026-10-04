import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { StockLotService } from '../service/stock-lot.service';
import { IStockLot } from '../stock-lot.model';

@Component({
  templateUrl: './stock-lot-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class StockLotDeleteDialog {
  stockLot?: IStockLot;

  protected readonly stockLotService = inject(StockLotService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stockLotService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
