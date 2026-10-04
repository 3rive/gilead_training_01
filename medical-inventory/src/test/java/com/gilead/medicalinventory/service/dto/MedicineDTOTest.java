package com.gilead.medicalinventory.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gilead.medicalinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicineDTO.class);
        MedicineDTO medicineDTO1 = new MedicineDTO();
        medicineDTO1.setId(1L);
        MedicineDTO medicineDTO2 = new MedicineDTO();
        assertThat(medicineDTO1).isNotEqualTo(medicineDTO2);
        medicineDTO2.setId(medicineDTO1.getId());
        assertThat(medicineDTO1).isEqualTo(medicineDTO2);
        medicineDTO2.setId(2L);
        assertThat(medicineDTO1).isNotEqualTo(medicineDTO2);
        medicineDTO1.setId(null);
        assertThat(medicineDTO1).isNotEqualTo(medicineDTO2);
    }
}
