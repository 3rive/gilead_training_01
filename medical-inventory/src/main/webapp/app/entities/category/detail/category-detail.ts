import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { ICategory } from '../category.model';

@Component({
  selector: 'mi-category-detail',
  templateUrl: './category-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class CategoryDetail {
  readonly category = input<ICategory | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
