package com.gilead.medicalinventory.service.mapper;

import static com.gilead.medicalinventory.domain.MedicineAsserts.*;
import static com.gilead.medicalinventory.domain.MedicineTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicineMapperTest {

    private MedicineMapper medicineMapper;

    @BeforeEach
    void setUp() {
        medicineMapper = new MedicineMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicineSample1();
        var actual = medicineMapper.toEntity(medicineMapper.toDto(expected));
        assertMedicineAllPropertiesEquals(expected, actual);
    }
}
