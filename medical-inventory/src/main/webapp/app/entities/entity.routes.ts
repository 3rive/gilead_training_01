import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'user-management',
    title: 'UserManagements',
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'authority',
    title: 'Authorities',
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'category',
    title: 'Categories',
    loadChildren: () => import('./category/category.routes'),
  },
  {
    path: 'supplier',
    title: 'Suppliers',
    loadChildren: () => import('./supplier/supplier.routes'),
  },
  {
    path: 'storage-location',
    title: 'StorageLocations',
    loadChildren: () => import('./storage-location/storage-location.routes'),
  },
  {
    path: 'medicine',
    title: 'Medicines',
    loadChildren: () => import('./medicine/medicine.routes'),
  },
  {
    path: 'stock-lot',
    title: 'StockLots',
    loadChildren: () => import('./stock-lot/stock-lot.routes'),
  },
  {
    path: 'stock-movement',
    title: 'StockMovements',
    loadChildren: () => import('./stock-movement/stock-movement.routes'),
  },
  // jhipster-needle-add-entity-route - JHipster will add entity modules routes here
];

export default routes;
