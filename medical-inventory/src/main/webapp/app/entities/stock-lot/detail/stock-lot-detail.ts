import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IStockLot } from '../stock-lot.model';

@Component({
  selector: 'mi-stock-lot-detail',
  templateUrl: './stock-lot-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink, FormatMediumDatePipe],
})
export class StockLotDetail {
  readonly stockLot = input<IStockLot | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
