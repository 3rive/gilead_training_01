import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import StorageLocationResolve from './route/storage-location-routing-resolve.service';

const storageLocationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/storage-location').then(m => m.StorageLocation),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/storage-location-detail').then(m => m.StorageLocationDetail),
    resolve: {
      storageLocation: StorageLocationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/storage-location-update').then(m => m.StorageLocationUpdate),
    resolve: {
      storageLocation: StorageLocationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/storage-location-update').then(m => m.StorageLocationUpdate),
    resolve: {
      storageLocation: StorageLocationResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default storageLocationRoute;
