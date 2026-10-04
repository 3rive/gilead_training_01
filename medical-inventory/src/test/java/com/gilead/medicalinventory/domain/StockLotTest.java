package com.gilead.medicalinventory.domain;

import static com.gilead.medicalinventory.domain.MedicineTestSamples.*;
import static com.gilead.medicalinventory.domain.StockLotTestSamples.*;
import static com.gilead.medicalinventory.domain.StorageLocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gilead.medicalinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockLotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockLot.class);
        StockLot stockLot1 = getStockLotSample1();
        StockLot stockLot2 = new StockLot();
        assertThat(stockLot1).isNotEqualTo(stockLot2);

        stockLot2.setId(stockLot1.getId());
        assertThat(stockLot1).isEqualTo(stockLot2);

        stockLot2 = getStockLotSample2();
        assertThat(stockLot1).isNotEqualTo(stockLot2);
    }

    @Test
    void medicineTest() {
        StockLot stockLot = getStockLotRandomSampleGenerator();
        Medicine medicineBack = getMedicineRandomSampleGenerator();

        stockLot.setMedicine(medicineBack);
        assertThat(stockLot.getMedicine()).isEqualTo(medicineBack);

        stockLot.medicine(null);
        assertThat(stockLot.getMedicine()).isNull();
    }

    @Test
    void storageLocationTest() {
        StockLot stockLot = getStockLotRandomSampleGenerator();
        StorageLocation storageLocationBack = getStorageLocationRandomSampleGenerator();

        stockLot.setStorageLocation(storageLocationBack);
        assertThat(stockLot.getStorageLocation()).isEqualTo(storageLocationBack);

        stockLot.storageLocation(null);
        assertThat(stockLot.getStorageLocation()).isNull();
    }
}
