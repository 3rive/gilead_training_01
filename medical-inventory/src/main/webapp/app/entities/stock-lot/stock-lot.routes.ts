import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import StockLotResolve from './route/stock-lot-routing-resolve.service';

const stockLotRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/stock-lot').then(m => m.StockLot),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/stock-lot-detail').then(m => m.StockLotDetail),
    resolve: {
      stockLot: StockLotResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/stock-lot-update').then(m => m.StockLotUpdate),
    resolve: {
      stockLot: StockLotResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/stock-lot-update').then(m => m.StockLotUpdate),
    resolve: {
      stockLot: StockLotResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default stockLotRoute;
