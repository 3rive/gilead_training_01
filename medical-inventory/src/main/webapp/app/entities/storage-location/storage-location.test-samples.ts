import { IStorageLocation, NewStorageLocation } from './storage-location.model';

export const sampleWithRequiredData: IStorageLocation = {
  id: 24792,
  code: 'through jealously oo',
  name: 'worldly',
  temperatureControlled: true,
};

export const sampleWithPartialData: IStorageLocation = {
  id: 371,
  code: 'frantically yum',
  name: 'the ew',
  temperatureControlled: true,
};

export const sampleWithFullData: IStorageLocation = {
  id: 27688,
  code: 'woot merrily',
  name: 'bandwidth',
  building: 'thigh brr',
  temperatureControlled: false,
};

export const sampleWithNewData: NewStorageLocation = {
  code: 'creator that',
  name: 'unimportant',
  temperatureControlled: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
