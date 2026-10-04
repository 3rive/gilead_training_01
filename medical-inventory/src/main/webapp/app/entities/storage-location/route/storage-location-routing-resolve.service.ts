import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { StorageLocationService } from '../service/storage-location.service';
import { IStorageLocation } from '../storage-location.model';

const storageLocationResolve = (route: ActivatedRouteSnapshot): Observable<null | IStorageLocation> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(StorageLocationService);
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

export default storageLocationResolve;
