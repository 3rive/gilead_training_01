import dayjs from 'dayjs/esm';

import { IStockLot, NewStockLot } from './stock-lot.model';

export const sampleWithRequiredData: IStockLot = {
  id: 27752,
  batchNumber: 'chime',
  expiryDate: dayjs('2023-12-18'),
  quantityOnHand: 8706,
  receivedDate: dayjs('2023-12-18'),
};

export const sampleWithPartialData: IStockLot = {
  id: 13023,
  batchNumber: 'until',
  expiryDate: dayjs('2023-12-18'),
  quantityOnHand: 11650,
  receivedDate: dayjs('2023-12-17'),
};

export const sampleWithFullData: IStockLot = {
  id: 13446,
  batchNumber: 'disinherit or',
  expiryDate: dayjs('2023-12-18'),
  quantityOnHand: 29492,
  receivedDate: dayjs('2023-12-17'),
};

export const sampleWithNewData: NewStockLot = {
  batchNumber: 'ripe ameliorate viciously',
  expiryDate: dayjs('2023-12-18'),
  quantityOnHand: 4749,
  receivedDate: dayjs('2023-12-18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
