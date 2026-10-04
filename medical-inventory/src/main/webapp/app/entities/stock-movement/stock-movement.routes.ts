import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import StockMovementResolve from './route/stock-movement-routing-resolve.service';

const stockMovementRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/stock-movement').then(m => m.StockMovement),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/stock-movement-detail').then(m => m.StockMovementDetail),
    resolve: {
      stockMovement: StockMovementResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/stock-movement-update').then(m => m.StockMovementUpdate),
    resolve: {
      stockMovement: StockMovementResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/stock-movement-update').then(m => m.StockMovementUpdate),
    resolve: {
      stockMovement: StockMovementResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default stockMovementRoute;
