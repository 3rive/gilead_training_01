package com.gilead.medicalinventory.service.mapper;

import com.gilead.medicalinventory.domain.Medicine;
import com.gilead.medicalinventory.domain.StockLot;
import com.gilead.medicalinventory.domain.StorageLocation;
import com.gilead.medicalinventory.service.dto.MedicineDTO;
import com.gilead.medicalinventory.service.dto.StockLotDTO;
import com.gilead.medicalinventory.service.dto.StorageLocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StockLot} and its DTO {@link StockLotDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockLotMapper extends EntityMapper<StockLotDTO, StockLot> {
    @Mapping(target = "medicine", source = "medicine", qualifiedByName = "medicineName")
    @Mapping(target = "storageLocation", source = "storageLocation", qualifiedByName = "storageLocationName")
    StockLotDTO toDto(StockLot s);

    @Named("medicineName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MedicineDTO toDtoMedicineName(Medicine medicine);

    @Named("storageLocationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    StorageLocationDTO toDtoStorageLocationName(StorageLocation storageLocation);
}
