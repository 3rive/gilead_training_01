package com.gilead.medicalinventory.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gilead.medicalinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockMovementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockMovementDTO.class);
        StockMovementDTO stockMovementDTO1 = new StockMovementDTO();
        stockMovementDTO1.setId(1L);
        StockMovementDTO stockMovementDTO2 = new StockMovementDTO();
        assertThat(stockMovementDTO1).isNotEqualTo(stockMovementDTO2);
        stockMovementDTO2.setId(stockMovementDTO1.getId());
        assertThat(stockMovementDTO1).isEqualTo(stockMovementDTO2);
        stockMovementDTO2.setId(2L);
        assertThat(stockMovementDTO1).isNotEqualTo(stockMovementDTO2);
        stockMovementDTO1.setId(null);
        assertThat(stockMovementDTO1).isNotEqualTo(stockMovementDTO2);
    }
}
