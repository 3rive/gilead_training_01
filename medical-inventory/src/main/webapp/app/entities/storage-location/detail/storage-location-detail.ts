import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { IStorageLocation } from '../storage-location.model';

@Component({
  selector: 'mi-storage-location-detail',
  templateUrl: './storage-location-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class StorageLocationDetail {
  readonly storageLocation = input<IStorageLocation | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
