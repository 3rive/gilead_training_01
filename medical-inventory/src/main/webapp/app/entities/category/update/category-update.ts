import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { ICategory } from '../category.model';
import { CategoryService } from '../service/category.service';

import { CategoryFormGroup, CategoryFormService } from './category-form.service';

@Component({
  selector: 'mi-category-update',
  templateUrl: './category-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CategoryUpdate implements OnInit {
  readonly isSaving = signal(false);
  category: ICategory | null = null;

  protected categoryService = inject(CategoryService);
  protected categoryFormService = inject(CategoryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CategoryFormGroup = this.categoryFormService.createCategoryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ category }) => {
      this.category = category;
      if (category) {
        this.updateForm(category);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const category = this.categoryFormService.getCategory(this.editForm);
    if (category.id === null) {
      this.subscribeToSaveResponse(this.categoryService.create(category));
    } else {
      this.subscribeToSaveResponse(this.categoryService.update(category));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICategory | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(category: ICategory): void {
    this.category = category;
    this.categoryFormService.resetForm(this.editForm, category);
  }
}
