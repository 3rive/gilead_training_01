package com.gilead.medicalinventory.web.rest;

import static com.gilead.medicalinventory.domain.MedicineAsserts.*;
import static com.gilead.medicalinventory.web.rest.TestUtil.createUpdateProxyForBean;
import static com.gilead.medicalinventory.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gilead.medicalinventory.IntegrationTest;
import com.gilead.medicalinventory.domain.Category;
import com.gilead.medicalinventory.domain.Medicine;
import com.gilead.medicalinventory.domain.enumeration.DosageForm;
import com.gilead.medicalinventory.repository.MedicineRepository;
import com.gilead.medicalinventory.service.MedicineService;
import com.gilead.medicalinventory.service.dto.MedicineDTO;
import com.gilead.medicalinventory.service.mapper.MedicineMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link MedicineResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MedicineResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final String DEFAULT_GENERIC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GENERIC_NAME = "BBBBBBBBBB";

    private static final DosageForm DEFAULT_DOSAGE_FORM = DosageForm.TABLET;
    private static final DosageForm UPDATED_DOSAGE_FORM = DosageForm.CAPSULE;

    private static final String DEFAULT_STRENGTH = "AAAAAAAAAA";
    private static final String UPDATED_STRENGTH = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(1);

    private static final Integer DEFAULT_REORDER_LEVEL = 0;
    private static final Integer UPDATED_REORDER_LEVEL = 1;

    private static final Boolean DEFAULT_CONTROLLED_SUBSTANCE = false;
    private static final Boolean UPDATED_CONTROLLED_SUBSTANCE = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medicines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicineRepository medicineRepository;

    @Mock
    private MedicineRepository medicineRepositoryMock;

    @Autowired
    private MedicineMapper medicineMapper;

    @Mock
    private MedicineService medicineServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicineMockMvc;

    private Medicine medicine;

    private Medicine insertedMedicine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicine createEntity(EntityManager em) {
        Medicine medicine = new Medicine()
            .name(DEFAULT_NAME)
            .sku(DEFAULT_SKU)
            .genericName(DEFAULT_GENERIC_NAME)
            .dosageForm(DEFAULT_DOSAGE_FORM)
            .strength(DEFAULT_STRENGTH)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .reorderLevel(DEFAULT_REORDER_LEVEL)
            .controlledSubstance(DEFAULT_CONTROLLED_SUBSTANCE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity();
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).getFirst();
        }
        medicine.setCategory(category);
        return medicine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicine createUpdatedEntity(EntityManager em) {
        Medicine updatedMedicine = new Medicine()
            .name(UPDATED_NAME)
            .sku(UPDATED_SKU)
            .genericName(UPDATED_GENERIC_NAME)
            .dosageForm(UPDATED_DOSAGE_FORM)
            .strength(UPDATED_STRENGTH)
            .unitPrice(UPDATED_UNIT_PRICE)
            .reorderLevel(UPDATED_REORDER_LEVEL)
            .controlledSubstance(UPDATED_CONTROLLED_SUBSTANCE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity();
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).getFirst();
        }
        updatedMedicine.setCategory(category);
        return updatedMedicine;
    }

    @BeforeEach
    void initTest() {
        medicine = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMedicine != null) {
            medicineRepository.delete(insertedMedicine);
            insertedMedicine = null;
        }
    }

    @Test
    @Transactional
    void createMedicine() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);
        var returnedMedicineDTO = om.readValue(
            restMedicineMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicineDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicineDTO.class
        );

        // Validate the Medicine in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicine = medicineMapper.toEntity(returnedMedicineDTO);
        assertMedicineUpdatableFieldsEquals(returnedMedicine, getPersistedMedicine(returnedMedicine));

        insertedMedicine = returnedMedicine;
    }

    @Test
    @Transactional
    void createMedicineWithExistingId() throws Exception {
        // Create the Medicine with an existing ID
        medicine.setId(1L);
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medicine in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicine.setName(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSkuIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicine.setSku(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDosageFormIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicine.setDosageForm(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicine.setUnitPrice(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReorderLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicine.setReorderLevel(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkControlledSubstanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicine.setControlledSubstance(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicines() throws Exception {
        // Initialize the database
        insertedMedicine = medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList
        restMedicineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].genericName").value(hasItem(DEFAULT_GENERIC_NAME)))
            .andExpect(jsonPath("$.[*].dosageForm").value(hasItem(DEFAULT_DOSAGE_FORM.toString())))
            .andExpect(jsonPath("$.[*].strength").value(hasItem(DEFAULT_STRENGTH)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].reorderLevel").value(hasItem(DEFAULT_REORDER_LEVEL)))
            .andExpect(jsonPath("$.[*].controlledSubstance").value(hasItem(DEFAULT_CONTROLLED_SUBSTANCE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicinesWithEagerRelationshipsIsEnabled() throws Exception {
        when(medicineServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicineMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(medicineServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicinesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(medicineServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicineMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(medicineRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMedicine() throws Exception {
        // Initialize the database
        insertedMedicine = medicineRepository.saveAndFlush(medicine);

        // Get the medicine
        restMedicineMockMvc
            .perform(get(ENTITY_API_URL_ID, medicine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicine.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.genericName").value(DEFAULT_GENERIC_NAME))
            .andExpect(jsonPath("$.dosageForm").value(DEFAULT_DOSAGE_FORM.toString()))
            .andExpect(jsonPath("$.strength").value(DEFAULT_STRENGTH))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.reorderLevel").value(DEFAULT_REORDER_LEVEL))
            .andExpect(jsonPath("$.controlledSubstance").value(DEFAULT_CONTROLLED_SUBSTANCE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingMedicine() throws Exception {
        // Get the medicine
        restMedicineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicine() throws Exception {
        // Initialize the database
        insertedMedicine = medicineRepository.saveAndFlush(medicine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicine
        Medicine updatedMedicine = medicineRepository.findById(medicine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicine are not directly saved in db
        em.detach(updatedMedicine);
        updatedMedicine
            .name(UPDATED_NAME)
            .sku(UPDATED_SKU)
            .genericName(UPDATED_GENERIC_NAME)
            .dosageForm(UPDATED_DOSAGE_FORM)
            .strength(UPDATED_STRENGTH)
            .unitPrice(UPDATED_UNIT_PRICE)
            .reorderLevel(UPDATED_REORDER_LEVEL)
            .controlledSubstance(UPDATED_CONTROLLED_SUBSTANCE)
            .description(UPDATED_DESCRIPTION);
        MedicineDTO medicineDTO = medicineMapper.toDto(updatedMedicine);

        restMedicineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medicine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicineToMatchAllProperties(updatedMedicine);
    }

    @Test
    @Transactional
    void putNonExistingMedicine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicine.setId(longCount.incrementAndGet());

        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicine.setId(longCount.incrementAndGet());

        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicine.setId(longCount.incrementAndGet());

        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicineWithPatch() throws Exception {
        // Initialize the database
        insertedMedicine = medicineRepository.saveAndFlush(medicine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicine using partial update
        Medicine partialUpdatedMedicine = new Medicine();
        partialUpdatedMedicine.setId(medicine.getId());

        partialUpdatedMedicine.name(UPDATED_NAME).dosageForm(UPDATED_DOSAGE_FORM).strength(UPDATED_STRENGTH);

        restMedicineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicine))
            )
            .andExpect(status().isOk());

        // Validate the Medicine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicineUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMedicine, medicine), getPersistedMedicine(medicine));
    }

    @Test
    @Transactional
    void fullUpdateMedicineWithPatch() throws Exception {
        // Initialize the database
        insertedMedicine = medicineRepository.saveAndFlush(medicine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicine using partial update
        Medicine partialUpdatedMedicine = new Medicine();
        partialUpdatedMedicine.setId(medicine.getId());

        partialUpdatedMedicine
            .name(UPDATED_NAME)
            .sku(UPDATED_SKU)
            .genericName(UPDATED_GENERIC_NAME)
            .dosageForm(UPDATED_DOSAGE_FORM)
            .strength(UPDATED_STRENGTH)
            .unitPrice(UPDATED_UNIT_PRICE)
            .reorderLevel(UPDATED_REORDER_LEVEL)
            .controlledSubstance(UPDATED_CONTROLLED_SUBSTANCE)
            .description(UPDATED_DESCRIPTION);

        restMedicineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicine))
            )
            .andExpect(status().isOk());

        // Validate the Medicine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicineUpdatableFieldsEquals(partialUpdatedMedicine, getPersistedMedicine(partialUpdatedMedicine));
    }

    @Test
    @Transactional
    void patchNonExistingMedicine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicine.setId(longCount.incrementAndGet());

        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicine.setId(longCount.incrementAndGet());

        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicine.setId(longCount.incrementAndGet());

        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicine() throws Exception {
        // Initialize the database
        insertedMedicine = medicineRepository.saveAndFlush(medicine);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicine
        restMedicineMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicineRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Medicine getPersistedMedicine(Medicine medicine) {
        return medicineRepository.findById(medicine.getId()).orElseThrow();
    }

    protected void assertPersistedMedicineToMatchAllProperties(Medicine expectedMedicine) {
        assertMedicineAllPropertiesEquals(expectedMedicine, getPersistedMedicine(expectedMedicine));
    }

    protected void assertPersistedMedicineToMatchUpdatableProperties(Medicine expectedMedicine) {
        assertMedicineAllUpdatablePropertiesEquals(expectedMedicine, getPersistedMedicine(expectedMedicine));
    }
}
