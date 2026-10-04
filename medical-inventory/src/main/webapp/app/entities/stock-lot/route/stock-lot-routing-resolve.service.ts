import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { StockLotService } from '../service/stock-lot.service';
import { IStockLot } from '../stock-lot.model';

const stockLotResolve = (route: ActivatedRouteSnapshot): Observable<null | IStockLot> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(StockLotService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default stockLotResolve;
