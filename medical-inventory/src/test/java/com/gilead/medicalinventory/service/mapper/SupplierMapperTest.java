package com.gilead.medicalinventory.service.mapper;

import static com.gilead.medicalinventory.domain.SupplierAsserts.*;
import static com.gilead.medicalinventory.domain.SupplierTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplierMapperTest {

    private SupplierMapper supplierMapper;

    @BeforeEach
    void setUp() {
        supplierMapper = new SupplierMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSupplierSample1();
        var actual = supplierMapper.toEntity(supplierMapper.toDto(expected));
        assertSupplierAllPropertiesEquals(expected, actual);
    }
}
