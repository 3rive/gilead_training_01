package com.gilead.medicalinventory.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gilead.medicalinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockLotDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockLotDTO.class);
        StockLotDTO stockLotDTO1 = new StockLotDTO();
        stockLotDTO1.setId(1L);
        StockLotDTO stockLotDTO2 = new StockLotDTO();
        assertThat(stockLotDTO1).isNotEqualTo(stockLotDTO2);
        stockLotDTO2.setId(stockLotDTO1.getId());
        assertThat(stockLotDTO1).isEqualTo(stockLotDTO2);
        stockLotDTO2.setId(2L);
        assertThat(stockLotDTO1).isNotEqualTo(stockLotDTO2);
        stockLotDTO1.setId(null);
        assertThat(stockLotDTO1).isNotEqualTo(stockLotDTO2);
    }
}
