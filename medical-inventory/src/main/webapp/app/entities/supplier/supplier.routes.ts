import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import SupplierResolve from './route/supplier-routing-resolve.service';

const supplierRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/supplier').then(m => m.Supplier),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/supplier-detail').then(m => m.SupplierDetail),
    resolve: {
      supplier: SupplierResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/supplier-update').then(m => m.SupplierUpdate),
    resolve: {
      supplier: SupplierResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/supplier-update').then(m => m.SupplierUpdate),
    resolve: {
      supplier: SupplierResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default supplierRoute;
