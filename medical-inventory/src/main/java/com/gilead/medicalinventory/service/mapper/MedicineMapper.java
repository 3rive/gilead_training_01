package com.gilead.medicalinventory.service.mapper;

import com.gilead.medicalinventory.domain.Category;
import com.gilead.medicalinventory.domain.Medicine;
import com.gilead.medicalinventory.domain.Supplier;
import com.gilead.medicalinventory.service.dto.CategoryDTO;
import com.gilead.medicalinventory.service.dto.MedicineDTO;
import com.gilead.medicalinventory.service.dto.SupplierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medicine} and its DTO {@link MedicineDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicineMapper extends EntityMapper<MedicineDTO, Medicine> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryName")
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "supplierName")
    MedicineDTO toDto(Medicine s);

    @Named("categoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryDTO toDtoCategoryName(Category category);

    @Named("supplierName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SupplierDTO toDtoSupplierName(Supplier supplier);
}
