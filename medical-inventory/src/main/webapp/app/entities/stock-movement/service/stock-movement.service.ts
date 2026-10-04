import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IStockMovement, NewStockMovement } from '../stock-movement.model';

export type PartialUpdateStockMovement = Partial<IStockMovement> & Pick<IStockMovement, 'id'>;

type RestOf<T extends IStockMovement | NewStockMovement> = Omit<T, 'occurredAt'> & {
  occurredAt?: string | null;
};

export type RestStockMovement = RestOf<IStockMovement>;

export type NewRestStockMovement = RestOf<NewStockMovement>;

export type PartialUpdateRestStockMovement = RestOf<PartialUpdateStockMovement>;

@Service()
export class StockMovementsService {
  readonly stockMovementsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly stockMovementsResource = httpResource<RestStockMovement[]>(() => {
    const params = this.stockMovementsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of stockMovement that have been fetched. It is updated when the stockMovementsResource emits a new value.
   * In case of error while fetching the stockMovements, the signal is set to an empty array.
   */
  readonly stockMovements = computed(() =>
    (this.stockMovementsResource.hasValue() ? this.stockMovementsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/stock-movements`;

  protected convertValueFromServer(restStockMovement: RestStockMovement): IStockMovement {
    return {
      ...restStockMovement,
      occurredAt: restStockMovement.occurredAt ? dayjs(restStockMovement.occurredAt) : undefined,
    };
  }
}

@Service()
export class StockMovementService extends StockMovementsService {
  protected readonly http = inject(HttpClient);

  create(stockMovement: NewStockMovement): Observable<IStockMovement> {
    const copy = this.convertValueFromClient(stockMovement);
    return this.http.post<RestStockMovement>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(stockMovement: IStockMovement): Observable<IStockMovement> {
    const copy = this.convertValueFromClient(stockMovement);
    return this.http
      .put<RestStockMovement>(`${this.resourceUrl}/${encodeURIComponent(this.getStockMovementIdentifier(stockMovement))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(stockMovement: PartialUpdateStockMovement): Observable<IStockMovement> {
    const copy = this.convertValueFromClient(stockMovement);
    return this.http
      .patch<RestStockMovement>(`${this.resourceUrl}/${encodeURIComponent(this.getStockMovementIdentifier(stockMovement))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IStockMovement> {
    return this.http
      .get<RestStockMovement>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IStockMovement[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestStockMovement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getStockMovementIdentifier(stockMovement: Pick<IStockMovement, 'id'>): number {
    return stockMovement.id;
  }

  compareStockMovement(o1: Pick<IStockMovement, 'id'> | null, o2: Pick<IStockMovement, 'id'> | null): boolean {
    return o1 && o2 ? this.getStockMovementIdentifier(o1) === this.getStockMovementIdentifier(o2) : o1 === o2;
  }

  addStockMovementToCollectionIfMissing<Type extends Pick<IStockMovement, 'id'>>(
    stockMovementCollection: Type[],
    ...stockMovementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stockMovements: Type[] = stockMovementsToCheck.filter(
      stockMovementItem => stockMovementItem !== null && stockMovementItem !== undefined,
    );
    if (stockMovements.length > 0) {
      const stockMovementCollectionIdentifiers = stockMovementCollection.map(stockMovementItem =>
        this.getStockMovementIdentifier(stockMovementItem),
      );
      const stockMovementsToAdd = stockMovements.filter(stockMovementItem => {
        const stockMovementIdentifier = this.getStockMovementIdentifier(stockMovementItem);
        if (stockMovementCollectionIdentifiers.includes(stockMovementIdentifier)) {
          return false;
        }
        stockMovementCollectionIdentifiers.push(stockMovementIdentifier);
        return true;
      });
      return [...stockMovementsToAdd, ...stockMovementCollection];
    }
    return stockMovementCollection;
  }

  protected convertValueFromClient<T extends IStockMovement | NewStockMovement | PartialUpdateStockMovement>(stockMovement: T): RestOf<T> {
    return {
      ...stockMovement,
      occurredAt: stockMovement.occurredAt?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestStockMovement): IStockMovement {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestStockMovement[]): IStockMovement[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
