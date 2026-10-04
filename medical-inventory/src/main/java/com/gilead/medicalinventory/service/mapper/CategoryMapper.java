package com.gilead.medicalinventory.service.mapper;

import com.gilead.medicalinventory.domain.Category;
import com.gilead.medicalinventory.service.dto.CategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {}
