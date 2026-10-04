import dayjs from 'dayjs/esm';

import { IMedicine } from 'app/entities/medicine/medicine.model';
import { IStorageLocation } from 'app/entities/storage-location/storage-location.model';

export interface IStockLot {
  id: number;
  batchNumber?: string | null;
  expiryDate?: dayjs.Dayjs | null;
  quantityOnHand?: number | null;
  receivedDate?: dayjs.Dayjs | null;
  medicine?: Pick<IMedicine, 'id' | 'name'> | null;
  storageLocation?: Pick<IStorageLocation, 'id' | 'name'> | null;
}

export type NewStockLot = Omit<IStockLot, 'id'> & { id: null };
