import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CategoryResolve from './route/category-routing-resolve.service';

const categoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/category').then(m => m.Category),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/category-detail').then(m => m.CategoryDetail),
    resolve: {
      category: CategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/category-update').then(m => m.CategoryUpdate),
    resolve: {
      category: CategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/category-update').then(m => m.CategoryUpdate),
    resolve: {
      category: CategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default categoryRoute;
