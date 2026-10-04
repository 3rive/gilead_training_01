import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { ISupplier } from '../supplier.model';

@Component({
  selector: 'mi-supplier-detail',
  templateUrl: './supplier-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class SupplierDetail {
  readonly supplier = input<ISupplier | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
