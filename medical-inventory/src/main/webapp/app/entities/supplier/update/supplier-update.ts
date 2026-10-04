import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { SupplierService } from '../service/supplier.service';
import { ISupplier } from '../supplier.model';

import { SupplierFormGroup, SupplierFormService } from './supplier-form.service';

@Component({
  selector: 'mi-supplier-update',
  templateUrl: './supplier-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class SupplierUpdate implements OnInit {
  readonly isSaving = signal(false);
  supplier: ISupplier | null = null;

  protected supplierService = inject(SupplierService);
  protected supplierFormService = inject(SupplierFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SupplierFormGroup = this.supplierFormService.createSupplierFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ supplier }) => {
      this.supplier = supplier;
      if (supplier) {
        this.updateForm(supplier);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const supplier = this.supplierFormService.getSupplier(this.editForm);
    if (supplier.id === null) {
      this.subscribeToSaveResponse(this.supplierService.create(supplier));
    } else {
      this.subscribeToSaveResponse(this.supplierService.update(supplier));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ISupplier | null>): void {
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

  protected updateForm(supplier: ISupplier): void {
    this.supplier = supplier;
    this.supplierFormService.resetForm(this.editForm, supplier);
  }
}
