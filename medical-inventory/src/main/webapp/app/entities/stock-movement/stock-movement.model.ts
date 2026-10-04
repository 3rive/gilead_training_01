import dayjs from 'dayjs/esm';

import { MovementType } from 'app/entities/enumerations/movement-type.model';
import { IStockLot } from 'app/entities/stock-lot/stock-lot.model';

export interface IStockMovement {
  id: number;
  movementType?: keyof typeof MovementType | null;
  quantity?: number | null;
  occurredAt?: dayjs.Dayjs | null;
  reason?: string | null;
  referenceNumber?: string | null;
  stockLot?: Pick<IStockLot, 'id' | 'batchNumber'> | null;
}

export type NewStockMovement = Omit<IStockMovement, 'id'> & { id: null };
