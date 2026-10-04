package com.gilead.medicalinventory.domain;

import static com.gilead.medicalinventory.domain.CategoryTestSamples.*;
import static com.gilead.medicalinventory.domain.MedicineTestSamples.*;
import static com.gilead.medicalinventory.domain.SupplierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gilead.medicalinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medicine.class);
        Medicine medicine1 = getMedicineSample1();
        Medicine medicine2 = new Medicine();
        assertThat(medicine1).isNotEqualTo(medicine2);

        medicine2.setId(medicine1.getId());
        assertThat(medicine1).isEqualTo(medicine2);

        medicine2 = getMedicineSample2();
        assertThat(medicine1).isNotEqualTo(medicine2);
    }

    @Test
    void categoryTest() {
        Medicine medicine = getMedicineRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        medicine.setCategory(categoryBack);
        assertThat(medicine.getCategory()).isEqualTo(categoryBack);

        medicine.category(null);
        assertThat(medicine.getCategory()).isNull();
    }

    @Test
    void supplierTest() {
        Medicine medicine = getMedicineRandomSampleGenerator();
        Supplier supplierBack = getSupplierRandomSampleGenerator();

        medicine.setSupplier(supplierBack);
        assertThat(medicine.getSupplier()).isEqualTo(supplierBack);

        medicine.supplier(null);
        assertThat(medicine.getSupplier()).isNull();
    }
}
