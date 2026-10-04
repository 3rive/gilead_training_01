import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { SupplierService } from '../service/supplier.service';
import { ISupplier } from '../supplier.model';

const supplierResolve = (route: ActivatedRouteSnapshot): Observable<null | ISupplier> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(SupplierService);
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

export default supplierResolve;
