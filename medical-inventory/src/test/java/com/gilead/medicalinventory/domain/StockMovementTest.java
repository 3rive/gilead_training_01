package com.gilead.medicalinventory.domain;

import static com.gilead.medicalinventory.domain.StockLotTestSamples.*;
import static com.gilead.medicalinventory.domain.StockMovementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gilead.medicalinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockMovementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockMovement.class);
        StockMovement stockMovement1 = getStockMovementSample1();
        StockMovement stockMovement2 = new StockMovement();
        assertThat(stockMovement1).isNotEqualTo(stockMovement2);

        stockMovement2.setId(stockMovement1.getId());
        assertThat(stockMovement1).isEqualTo(stockMovement2);

        stockMovement2 = getStockMovementSample2();
        assertThat(stockMovement1).isNotEqualTo(stockMovement2);
    }

    @Test
    void stockLotTest() {
        StockMovement stockMovement = getStockMovementRandomSampleGenerator();
        StockLot stockLotBack = getStockLotRandomSampleGenerator();

        stockMovement.setStockLot(stockLotBack);
        assertThat(stockMovement.getStockLot()).isEqualTo(stockLotBack);

        stockMovement.stockLot(null);
        assertThat(stockMovement.getStockLot()).isNull();
    }
}
