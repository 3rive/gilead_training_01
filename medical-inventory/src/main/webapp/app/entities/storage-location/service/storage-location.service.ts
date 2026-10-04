import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IStorageLocation, NewStorageLocation } from '../storage-location.model';

export type PartialUpdateStorageLocation = Partial<IStorageLocation> & Pick<IStorageLocation, 'id'>;

@Service()
export class StorageLocationsService {
  readonly storageLocationsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly storageLocationsResource = httpResource<IStorageLocation[]>(() => {
    const params = this.storageLocationsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of storageLocation that have been fetched. It is updated when the storageLocationsResource emits a new value.
   * In case of error while fetching the storageLocations, the signal is set to an empty array.
   */
  readonly storageLocations = computed(() => (this.storageLocationsResource.hasValue() ? this.storageLocationsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/storage-locations`;
}

@Service()
export class StorageLocationService extends StorageLocationsService {
  protected readonly http = inject(HttpClient);

  create(storageLocation: NewStorageLocation): Observable<IStorageLocation> {
    return this.http.post<IStorageLocation>(this.resourceUrl, storageLocation);
  }

  update(storageLocation: IStorageLocation): Observable<IStorageLocation> {
    return this.http.put<IStorageLocation>(
      `${this.resourceUrl}/${encodeURIComponent(this.getStorageLocationIdentifier(storageLocation))}`,
      storageLocation,
    );
  }

  partialUpdate(storageLocation: PartialUpdateStorageLocation): Observable<IStorageLocation> {
    return this.http.patch<IStorageLocation>(
      `${this.resourceUrl}/${encodeURIComponent(this.getStorageLocationIdentifier(storageLocation))}`,
      storageLocation,
    );
  }

  find(id: number): Observable<IStorageLocation> {
    return this.http.get<IStorageLocation>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IStorageLocation[]>> {
    const options = createRequestOption(req);
    return this.http.get<IStorageLocation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getStorageLocationIdentifier(storageLocation: Pick<IStorageLocation, 'id'>): number {
    return storageLocation.id;
  }

  compareStorageLocation(o1: Pick<IStorageLocation, 'id'> | null, o2: Pick<IStorageLocation, 'id'> | null): boolean {
    return o1 && o2 ? this.getStorageLocationIdentifier(o1) === this.getStorageLocationIdentifier(o2) : o1 === o2;
  }

  addStorageLocationToCollectionIfMissing<Type extends Pick<IStorageLocation, 'id'>>(
    storageLocationCollection: Type[],
    ...storageLocationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const storageLocations: Type[] = storageLocationsToCheck.filter(
      storageLocationItem => storageLocationItem !== null && storageLocationItem !== undefined,
    );
    if (storageLocations.length > 0) {
      const storageLocationCollectionIdentifiers = storageLocationCollection.map(storageLocationItem =>
        this.getStorageLocationIdentifier(storageLocationItem),
      );
      const storageLocationsToAdd = storageLocations.filter(storageLocationItem => {
        const storageLocationIdentifier = this.getStorageLocationIdentifier(storageLocationItem);
        if (storageLocationCollectionIdentifiers.includes(storageLocationIdentifier)) {
          return false;
        }
        storageLocationCollectionIdentifiers.push(storageLocationIdentifier);
        return true;
      });
      return [...storageLocationsToAdd, ...storageLocationCollection];
    }
    return storageLocationCollection;
  }
}
