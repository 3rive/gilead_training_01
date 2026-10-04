package com.gilead.medicalinventory.service.mapper;

import com.gilead.medicalinventory.domain.StorageLocation;
import com.gilead.medicalinventory.service.dto.StorageLocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StorageLocation} and its DTO {@link StorageLocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface StorageLocationMapper extends EntityMapper<StorageLocationDTO, StorageLocation> {}
