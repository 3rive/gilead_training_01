import { IMedicine, NewMedicine } from './medicine.model';

export const sampleWithRequiredData: IMedicine = {
  id: 32678,
  name: 'citizen acidly although',
  sku: 'innovation midst',
  dosageForm: 'CREAM',
  unitPrice: 12911.22,
  reorderLevel: 23443,
  controlledSubstance: false,
};

export const sampleWithPartialData: IMedicine = {
  id: 18851,
  name: 'whoever sweetly and',
  sku: 'athwart',
  genericName: 'apparatus unnaturally',
  dosageForm: 'INJECTION',
  strength: 'yuck',
  unitPrice: 23564.5,
  reorderLevel: 711,
  controlledSubstance: false,
};

export const sampleWithFullData: IMedicine = {
  id: 5088,
  name: 'scared',
  sku: 'waver than',
  genericName: 'gadzooks while unabashedly',
  dosageForm: 'INJECTION',
  strength: 'celebrate',
  unitPrice: 17151.12,
  reorderLevel: 5938,
  controlledSubstance: true,
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewMedicine = {
  name: 'yet and archive',
  sku: 'gullible giggle',
  dosageForm: 'OTHER',
  unitPrice: 1585.37,
  reorderLevel: 8910,
  controlledSubstance: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
