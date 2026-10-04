package com.gilead.medicalinventory.service.mapper;

import com.gilead.medicalinventory.domain.StockLot;
import com.gilead.medicalinventory.domain.StockMovement;
import com.gilead.medicalinventory.service.dto.StockLotDTO;
import com.gilead.medicalinventory.service.dto.StockMovementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StockMovement} and its DTO {@link StockMovementDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockMovementMapper extends EntityMapper<StockMovementDTO, StockMovement> {
    @Mapping(target = "stockLot", source = "stockLot", qualifiedByName = "stockLotBatchNumber")
    StockMovementDTO toDto(StockMovement s);

    @Named("stockLotBatchNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "batchNumber", source = "batchNumber")
    StockLotDTO toDtoStockLotBatchNumber(StockLot stockLot);
}
