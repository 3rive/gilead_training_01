package com.gilead.medicalinventory.service.mapper;

import static com.gilead.medicalinventory.domain.StockLotAsserts.*;
import static com.gilead.medicalinventory.domain.StockLotTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StockLotMapperTest {

    private StockLotMapper stockLotMapper;

    @BeforeEach
    void setUp() {
        stockLotMapper = new StockLotMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStockLotSample1();
        var actual = stockLotMapper.toEntity(stockLotMapper.toDto(expected));
        assertStockLotAllPropertiesEquals(expected, actual);
    }
}
