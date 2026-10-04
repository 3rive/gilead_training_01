import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { ICategory } from '../category.model';
import { CategoryService } from '../service/category.service';

@Component({
  templateUrl: './category-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class CategoryDeleteDialog {
  category?: ICategory;

  protected readonly categoryService = inject(CategoryService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.categoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
