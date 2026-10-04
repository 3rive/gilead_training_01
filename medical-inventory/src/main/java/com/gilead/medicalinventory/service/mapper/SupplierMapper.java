package com.gilead.medicalinventory.service.mapper;

import com.gilead.medicalinventory.domain.Supplier;
import com.gilead.medicalinventory.service.dto.SupplierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Supplier} and its DTO {@link SupplierDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierMapper extends EntityMapper<SupplierDTO, Supplier> {}
