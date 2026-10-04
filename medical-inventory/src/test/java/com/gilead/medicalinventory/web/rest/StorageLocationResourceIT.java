package com.gilead.medicalinventory.web.rest;

import static com.gilead.medicalinventory.domain.StorageLocationAsserts.*;
import static com.gilead.medicalinventory.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gilead.medicalinventory.IntegrationTest;
import com.gilead.medicalinventory.domain.StorageLocation;
import com.gilead.medicalinventory.repository.StorageLocationRepository;
import com.gilead.medicalinventory.service.dto.StorageLocationDTO;
import com.gilead.medicalinventory.service.mapper.StorageLocationMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link StorageLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StorageLocationResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BUILDING = "AAAAAAAAAA";
    private static final String UPDATED_BUILDING = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TEMPERATURE_CONTROLLED = false;
    private static final Boolean UPDATED_TEMPERATURE_CONTROLLED = true;

    private static final String ENTITY_API_URL = "/api/storage-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StorageLocationRepository storageLocationRepository;

    @Autowired
    private StorageLocationMapper storageLocationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStorageLocationMockMvc;

    private StorageLocation storageLocation;

    private StorageLocation insertedStorageLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageLocation createEntity() {
        return new StorageLocation()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .building(DEFAULT_BUILDING)
            .temperatureControlled(DEFAULT_TEMPERATURE_CONTROLLED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageLocation createUpdatedEntity() {
        return new StorageLocation()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .building(UPDATED_BUILDING)
            .temperatureControlled(UPDATED_TEMPERATURE_CONTROLLED);
    }

    @BeforeEach
    void initTest() {
        storageLocation = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedStorageLocation != null) {
            storageLocationRepository.delete(insertedStorageLocation);
            insertedStorageLocation = null;
        }
    }

    @Test
    @Transactional
    void createStorageLocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StorageLocation
        StorageLocationDTO storageLocationDTO = storageLocationMapper.toDto(storageLocation);
        var returnedStorageLocationDTO = om.readValue(
            restStorageLocationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageLocationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StorageLocationDTO.class
        );

        // Validate the StorageLocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStorageLocation = storageLocationMapper.toEntity(returnedStorageLocationDTO);
        assertStorageLocationUpdatableFieldsEquals(returnedStorageLocation, getPersistedStorageLocation(returnedStorageLocation));

        insertedStorageLocation = returnedStorageLocation;
    }

    @Test
    @Transactional
    void createStorageLocationWithExistingId() throws Exception {
        // Create the StorageLocation with an existing ID
        storageLocation.setId(1L);
        StorageLocationDTO storageLocationDTO = storageLocationMapper.toDto(storageLocation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStorageLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageLocationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StorageLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageLocation.setCode(null);

        // Create the StorageLocation, which fails.
        StorageLocationDTO storageLocationDTO = storageLocationMapper.toDto(storageLocation);

        restStorageLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageLocationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageLocation.setName(null);

        // Create the StorageLocation, which fails.
        StorageLocationDTO storageLocationDTO = storageLocationMapper.toDto(storageLocation);

        restStorageLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageLocationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTemperatureControlledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageLocation.setTemperatureControlled(null);

        // Create the StorageLocation, which fails.
        StorageLocationDTO storageLocationDTO = storageLocationMapper.toDto(storageLocation);

        restStorageLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageLocationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStorageLocations() throws Exception {
        // Initialize the database
        insertedStorageLocation = storageLocationRepository.saveAndFlush(storageLocation);

        // Get all the storageLocationList
        restStorageLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storageLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].building").value(hasItem(DEFAULT_BUILDING)))
            .andExpect(jsonPath("$.[*].temperatureControlled").value(hasItem(DEFAULT_TEMPERATURE_CONTROLLED)));
    }

    @Test
    @Transactional
    void getStorageLocation() throws Exception {
        // Initialize the database
        insertedStorageLocation = storageLocationRepository.saveAndFlush(storageLocation);

        // Get the storageLocation
        restStorageLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, storageLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(storageLocation.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.building").value(DEFAULT_BUILDING))
            .andExpect(jsonPath("$.temperatureControlled").value(DEFAULT_TEMPERATURE_CONTROLLED));
    }

    @Test
    @Transactional
    void getNonExistingStorageLocation() throws Exception {
        // Get the storageLocation
        restStorageLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStorageLocation() throws Exception {
        // Initialize the database
        insertedStorageLocation = storageLocationRepository.saveAndFlush(storageLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storageLocation
        StorageLocation updatedStorageLocation = storageLocationRepository.findById(storageLocation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStorageLocation are not directly saved in db
        em.detach(updatedStorageLocation);
        updatedStorageLocation
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .building(UPDATED_BUILDING)
            .temperatureControlled(UPDATED_TEMPERATURE_CONTROLLED);
        StorageLocationDTO storageLocationDTO = storageLocationMapper.toDto(updatedStorageLocation);

        restStorageLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(storageLocationDTO))
            )
            .andExpect(status().isOk());

        // Validate the StorageLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStorageLocationToMatchAllProperties(updatedStorageLocation);
    }

    @Test
    @Transactional
    void putNonExistingStorageLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageLocation.setId(longCount.incrementAndGet());

        // Create the StorageLocation
        StorageLocationDTO storageLocationDTO = storageLocationMapper.toDto(storageLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(storageLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStorageLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageLocation.setId(longCount.incrementAndGet());

        // Create the StorageLocation
        StorageLocationDTO storageLocationDTO = storageLocationMapper.toDto(storageLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(storageLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStorageLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageLocation.setId(longCount.incrementAndGet());

        // Create the StorageLocation
        StorageLocationDTO storageLocationDTO = storageLocationMapper.toDto(storageLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageLocationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StorageLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStorageLocationWithPatch() throws Exception {
        // Initialize the database
        insertedStorageLocation = storageLocationRepository.saveAndFlush(storageLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storageLocation using partial update
        StorageLocation partialUpdatedStorageLocation = new StorageLocation();
        partialUpdatedStorageLocation.setId(storageLocation.getId());

        restStorageLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorageLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStorageLocation))
            )
            .andExpect(status().isOk());

        // Validate the StorageLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStorageLocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStorageLocation, storageLocation),
            getPersistedStorageLocation(storageLocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateStorageLocationWithPatch() throws Exception {
        // Initialize the database
        insertedStorageLocation = storageLocationRepository.saveAndFlush(storageLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storageLocation using partial update
        StorageLocation partialUpdatedStorageLocation = new StorageLocation();
        partialUpdatedStorageLocation.setId(storageLocation.getId());

        partialUpdatedStorageLocation
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .building(UPDATED_BUILDING)
            .temperatureControlled(UPDATED_TEMPERATURE_CONTROLLED);

        restStorageLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorageLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStorageLocation))
            )
            .andExpect(status().isOk());

        // Validate the StorageLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStorageLocationUpdatableFieldsEquals(
            partialUpdatedStorageLocation,
            getPersistedStorageLocation(partialUpdatedStorageLocation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingStorageLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageLocation.setId(longCount.incrementAndGet());

        // Create the StorageLocation
        StorageLocationDTO storageLocationDTO = storageLocationMapper.toDto(storageLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storageLocationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(storageLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStorageLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageLocation.setId(longCount.incrementAndGet());

        // Create the StorageLocation
        StorageLocationDTO storageLocationDTO = storageLocationMapper.toDto(storageLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(storageLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStorageLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageLocation.setId(longCount.incrementAndGet());

        // Create the StorageLocation
        StorageLocationDTO storageLocationDTO = storageLocationMapper.toDto(storageLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageLocationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(storageLocationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StorageLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStorageLocation() throws Exception {
        // Initialize the database
        insertedStorageLocation = storageLocationRepository.saveAndFlush(storageLocation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the storageLocation
        restStorageLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, storageLocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return storageLocationRepository.count();
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

    protected StorageLocation getPersistedStorageLocation(StorageLocation storageLocation) {
        return storageLocationRepository.findById(storageLocation.getId()).orElseThrow();
    }

    protected void assertPersistedStorageLocationToMatchAllProperties(StorageLocation expectedStorageLocation) {
        assertStorageLocationAllPropertiesEquals(expectedStorageLocation, getPersistedStorageLocation(expectedStorageLocation));
    }

    protected void assertPersistedStorageLocationToMatchUpdatableProperties(StorageLocation expectedStorageLocation) {
        assertStorageLocationAllUpdatablePropertiesEquals(expectedStorageLocation, getPersistedStorageLocation(expectedStorageLocation));
    }
}
