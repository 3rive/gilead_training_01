import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IStockMovement } from '../stock-movement.model';

@Component({
  selector: 'mi-stock-movement-detail',
  templateUrl: './stock-movement-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink, FormatMediumDatetimePipe],
})
export class StockMovementDetail {
  readonly stockMovement = input<IStockMovement | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
