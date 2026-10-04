import { ISupplier, NewSupplier } from './supplier.model';

export const sampleWithRequiredData: ISupplier = {
  id: 22717,
  name: 'menacing',
  active: false,
};

export const sampleWithPartialData: ISupplier = {
  id: 32611,
  name: 'of',
  phone: '(649) 705-9299 x4318',
  active: false,
};

export const sampleWithFullData: ISupplier = {
  id: 3840,
  name: 'cheerfully near',
  contactName: 'providence ick scholarship',
  email: 'Sylvan.Ritchie@gmail.com',
  phone: '(899) 421-3220',
  active: true,
};

export const sampleWithNewData: NewSupplier = {
  name: 'snack',
  active: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
