import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IStockLot, NewStockLot } from '../stock-lot.model';

export type PartialUpdateStockLot = Partial<IStockLot> & Pick<IStockLot, 'id'>;

type RestOf<T extends IStockLot | NewStockLot> = Omit<T, 'expiryDate' | 'receivedDate'> & {
  expiryDate?: string | null;
  receivedDate?: string | null;
};

export type RestStockLot = RestOf<IStockLot>;

export type NewRestStockLot = RestOf<NewStockLot>;

export type PartialUpdateRestStockLot = RestOf<PartialUpdateStockLot>;

@Service()
export class StockLotsService {
  readonly stockLotsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly stockLotsResource = httpResource<RestStockLot[]>(() => {
    const params = this.stockLotsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of stockLot that have been fetched. It is updated when the stockLotsResource emits a new value.
   * In case of error while fetching the stockLots, the signal is set to an empty array.
   */
  readonly stockLots = computed(() =>
    (this.stockLotsResource.hasValue() ? this.stockLotsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/stock-lots`;

  protected convertValueFromServer(restStockLot: RestStockLot): IStockLot {
    return {
      ...restStockLot,
      expiryDate: restStockLot.expiryDate ? dayjs(restStockLot.expiryDate) : undefined,
      receivedDate: restStockLot.receivedDate ? dayjs(restStockLot.receivedDate) : undefined,
    };
  }
}

@Service()
export class StockLotService extends StockLotsService {
  protected readonly http = inject(HttpClient);

  create(stockLot: NewStockLot): Observable<IStockLot> {
    const copy = this.convertValueFromClient(stockLot);
    return this.http.post<RestStockLot>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(stockLot: IStockLot): Observable<IStockLot> {
    const copy = this.convertValueFromClient(stockLot);
    return this.http
      .put<RestStockLot>(`${this.resourceUrl}/${encodeURIComponent(this.getStockLotIdentifier(stockLot))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(stockLot: PartialUpdateStockLot): Observable<IStockLot> {
    const copy = this.convertValueFromClient(stockLot);
    return this.http
      .patch<RestStockLot>(`${this.resourceUrl}/${encodeURIComponent(this.getStockLotIdentifier(stockLot))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IStockLot> {
    return this.http
      .get<RestStockLot>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IStockLot[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestStockLot[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getStockLotIdentifier(stockLot: Pick<IStockLot, 'id'>): number {
    return stockLot.id;
  }

  compareStockLot(o1: Pick<IStockLot, 'id'> | null, o2: Pick<IStockLot, 'id'> | null): boolean {
    return o1 && o2 ? this.getStockLotIdentifier(o1) === this.getStockLotIdentifier(o2) : o1 === o2;
  }

  addStockLotToCollectionIfMissing<Type extends Pick<IStockLot, 'id'>>(
    stockLotCollection: Type[],
    ...stockLotsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stockLots: Type[] = stockLotsToCheck.filter(stockLotItem => stockLotItem !== null && stockLotItem !== undefined);
    if (stockLots.length > 0) {
      const stockLotCollectionIdentifiers = stockLotCollection.map(stockLotItem => this.getStockLotIdentifier(stockLotItem));
      const stockLotsToAdd = stockLots.filter(stockLotItem => {
        const stockLotIdentifier = this.getStockLotIdentifier(stockLotItem);
        if (stockLotCollectionIdentifiers.includes(stockLotIdentifier)) {
          return false;
        }
        stockLotCollectionIdentifiers.push(stockLotIdentifier);
        return true;
      });
      return [...stockLotsToAdd, ...stockLotCollection];
    }
    return stockLotCollection;
  }

  protected convertValueFromClient<T extends IStockLot | NewStockLot | PartialUpdateStockLot>(stockLot: T): RestOf<T> {
    return {
      ...stockLot,
      expiryDate: stockLot.expiryDate?.format(DATE_FORMAT) ?? null,
      receivedDate: stockLot.receivedDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestStockLot): IStockLot {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestStockLot[]): IStockLot[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
