import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IMedicine, NewMedicine } from '../medicine.model';

export type PartialUpdateMedicine = Partial<IMedicine> & Pick<IMedicine, 'id'>;

@Service()
export class MedicinesService {
  readonly medicinesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly medicinesResource = httpResource<IMedicine[]>(() => {
    const params = this.medicinesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of medicine that have been fetched. It is updated when the medicinesResource emits a new value.
   * In case of error while fetching the medicines, the signal is set to an empty array.
   */
  readonly medicines = computed(() => (this.medicinesResource.hasValue() ? this.medicinesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/medicines`;
}

@Service()
export class MedicineService extends MedicinesService {
  protected readonly http = inject(HttpClient);

  create(medicine: NewMedicine): Observable<IMedicine> {
    return this.http.post<IMedicine>(this.resourceUrl, medicine);
  }

  update(medicine: IMedicine): Observable<IMedicine> {
    return this.http.put<IMedicine>(`${this.resourceUrl}/${encodeURIComponent(this.getMedicineIdentifier(medicine))}`, medicine);
  }

  partialUpdate(medicine: PartialUpdateMedicine): Observable<IMedicine> {
    return this.http.patch<IMedicine>(`${this.resourceUrl}/${encodeURIComponent(this.getMedicineIdentifier(medicine))}`, medicine);
  }

  find(id: number): Observable<IMedicine> {
    return this.http.get<IMedicine>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IMedicine[]>> {
    const options = createRequestOption(req);
    return this.http.get<IMedicine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getMedicineIdentifier(medicine: Pick<IMedicine, 'id'>): number {
    return medicine.id;
  }

  compareMedicine(o1: Pick<IMedicine, 'id'> | null, o2: Pick<IMedicine, 'id'> | null): boolean {
    return o1 && o2 ? this.getMedicineIdentifier(o1) === this.getMedicineIdentifier(o2) : o1 === o2;
  }

  addMedicineToCollectionIfMissing<Type extends Pick<IMedicine, 'id'>>(
    medicineCollection: Type[],
    ...medicinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const medicines: Type[] = medicinesToCheck.filter(medicineItem => medicineItem !== null && medicineItem !== undefined);
    if (medicines.length > 0) {
      const medicineCollectionIdentifiers = medicineCollection.map(medicineItem => this.getMedicineIdentifier(medicineItem));
      const medicinesToAdd = medicines.filter(medicineItem => {
        const medicineIdentifier = this.getMedicineIdentifier(medicineItem);
        if (medicineCollectionIdentifiers.includes(medicineIdentifier)) {
          return false;
        }
        medicineCollectionIdentifiers.push(medicineIdentifier);
        return true;
      });
      return [...medicinesToAdd, ...medicineCollection];
    }
    return medicineCollection;
  }
}
