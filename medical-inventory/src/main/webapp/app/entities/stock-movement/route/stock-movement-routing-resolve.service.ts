import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { StockMovementService } from '../service/stock-movement.service';
import { IStockMovement } from '../stock-movement.model';

const stockMovementResolve = (route: ActivatedRouteSnapshot): Observable<null | IStockMovement> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(StockMovementService);
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

export default stockMovementResolve;
