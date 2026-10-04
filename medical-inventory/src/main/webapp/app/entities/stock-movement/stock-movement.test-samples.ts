import dayjs from 'dayjs/esm';

import { IStockMovement, NewStockMovement } from './stock-movement.model';

export const sampleWithRequiredData: IStockMovement = {
  id: 16284,
  movementType: 'TRANSFER',
  quantity: 31826,
  occurredAt: dayjs('2023-12-18T02:44'),
};

export const sampleWithPartialData: IStockMovement = {
  id: 1186,
  movementType: 'EXPIRED',
  quantity: 16345,
  occurredAt: dayjs('2023-12-18T05:46'),
};

export const sampleWithFullData: IStockMovement = {
  id: 31835,
  movementType: 'DISPENSE',
  quantity: 24344,
  occurredAt: dayjs('2023-12-18T10:18'),
  reason: 'gah why',
  referenceNumber: 'clearly huzzah burdensome',
};

export const sampleWithNewData: NewStockMovement = {
  movementType: 'RECEIPT',
  quantity: 16643,
  occurredAt: dayjs('2023-12-17T19:47'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
