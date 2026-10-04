package com.gilead.medicalinventory.service.mapper;

import static com.gilead.medicalinventory.domain.StorageLocationAsserts.*;
import static com.gilead.medicalinventory.domain.StorageLocationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageLocationMapperTest {

    private StorageLocationMapper storageLocationMapper;

    @BeforeEach
    void setUp() {
        storageLocationMapper = new StorageLocationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStorageLocationSample1();
        var actual = storageLocationMapper.toEntity(storageLocationMapper.toDto(expected));
        assertStorageLocationAllPropertiesEquals(expected, actual);
    }
}
