package com.gilead.medicalinventory.domain;

import static com.gilead.medicalinventory.domain.StorageLocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gilead.medicalinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StorageLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageLocation.class);
        StorageLocation storageLocation1 = getStorageLocationSample1();
        StorageLocation storageLocation2 = new StorageLocation();
        assertThat(storageLocation1).isNotEqualTo(storageLocation2);

        storageLocation2.setId(storageLocation1.getId());
        assertThat(storageLocation1).isEqualTo(storageLocation2);

        storageLocation2 = getStorageLocationSample2();
        assertThat(storageLocation1).isNotEqualTo(storageLocation2);
    }
}
