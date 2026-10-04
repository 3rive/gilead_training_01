import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { DataUtils, EventManager, EventWithContent, FileLoadError } from 'app/core/util';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { DosageForm } from 'app/entities/enumerations/dosage-form.model';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { AlertError, AlertErrorModel } from 'app/shared/alert';
import { type BlobType } from 'app/shared/jhipster/data-utils';
import { IMedicine } from '../medicine.model';
import { MedicineService } from '../service/medicine.service';

import { MedicineFormGroup, MedicineFormService } from './medicine-form.service';

@Component({
  selector: 'mi-medicine-update',
  templateUrl: './medicine-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class MedicineUpdate implements OnInit {
  readonly isSaving = signal(false);
  medicine: IMedicine | null = null;
  dosageFormValues = Object.keys(DosageForm);

  categoriesSharedCollection = signal<ICategory[]>([]);
  suppliersSharedCollection = signal<ISupplier[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected medicineService = inject(MedicineService);
  protected medicineFormService = inject(MedicineFormService);
  protected categoryService = inject(CategoryService);
  protected supplierService = inject(SupplierService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MedicineFormGroup = this.medicineFormService.createMedicineFormGroup();

  compareCategory = (o1: ICategory | null, o2: ICategory | null): boolean => this.categoryService.compareCategory(o1, o2);

  compareSupplier = (o1: ISupplier | null, o2: ISupplier | null): boolean => this.supplierService.compareSupplier(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicine }) => {
      this.medicine = medicine;
      if (medicine) {
        this.updateForm(medicine);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined, blobType?: BlobType): void {
    this.dataUtils.openFile(base64String, contentType, blobType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('medicalinventoryApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const medicine = this.medicineFormService.getMedicine(this.editForm);
    if (medicine.id === null) {
      this.subscribeToSaveResponse(this.medicineService.create(medicine));
    } else {
      this.subscribeToSaveResponse(this.medicineService.update(medicine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IMedicine | null>): void {
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

  protected updateForm(medicine: IMedicine): void {
    this.medicine = medicine;
    this.medicineFormService.resetForm(this.editForm, medicine);

    this.categoriesSharedCollection.update(categories =>
      this.categoryService.addCategoryToCollectionIfMissing<ICategory>(categories, medicine.category),
    );
    this.suppliersSharedCollection.update(suppliers =>
      this.supplierService.addSupplierToCollectionIfMissing<ISupplier>(suppliers, medicine.supplier),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing<ICategory>(categories, this.medicine?.category),
        ),
      )
      .subscribe((categories: ICategory[]) => this.categoriesSharedCollection.set(categories));

    this.supplierService
      .query()
      .pipe(map((res: HttpResponse<ISupplier[]>) => res.body ?? []))
      .pipe(
        map((suppliers: ISupplier[]) =>
          this.supplierService.addSupplierToCollectionIfMissing<ISupplier>(suppliers, this.medicine?.supplier),
        ),
      )
      .subscribe((suppliers: ISupplier[]) => this.suppliersSharedCollection.set(suppliers));
  }
}
