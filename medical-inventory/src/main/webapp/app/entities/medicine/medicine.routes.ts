import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import MedicineResolve from './route/medicine-routing-resolve.service';

const medicineRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/medicine').then(m => m.Medicine),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/medicine-detail').then(m => m.MedicineDetail),
    resolve: {
      medicine: MedicineResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/medicine-update').then(m => m.MedicineUpdate),
    resolve: {
      medicine: MedicineResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/medicine-update').then(m => m.MedicineUpdate),
    resolve: {
      medicine: MedicineResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default medicineRoute;
