package com.gilead.medicalinventory.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gilead.medicalinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StorageLocationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageLocationDTO.class);
        StorageLocationDTO storageLocationDTO1 = new StorageLocationDTO();
        storageLocationDTO1.setId(1L);
        StorageLocationDTO storageLocationDTO2 = new StorageLocationDTO();
        assertThat(storageLocationDTO1).isNotEqualTo(storageLocationDTO2);
        storageLocationDTO2.setId(storageLocationDTO1.getId());
        assertThat(storageLocationDTO1).isEqualTo(storageLocationDTO2);
        storageLocationDTO2.setId(2L);
        assertThat(storageLocationDTO1).isNotEqualTo(storageLocationDTO2);
        storageLocationDTO1.setId(null);
        assertThat(storageLocationDTO1).isNotEqualTo(storageLocationDTO2);
    }
}
