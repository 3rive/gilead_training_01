package com.gilead.medicalinventory.web.rest;

import static com.gilead.medicalinventory.domain.StockLotAsserts.*;
import static com.gilead.medicalinventory.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gilead.medicalinventory.IntegrationTest;
import com.gilead.medicalinventory.domain.Medicine;
import com.gilead.medicalinventory.domain.StockLot;
import com.gilead.medicalinventory.domain.StorageLocation;
import com.gilead.medicalinventory.repository.StockLotRepository;
import com.gilead.medicalinventory.service.StockLotService;
import com.gilead.medicalinventory.service.dto.StockLotDTO;
import com.gilead.medicalinventory.service.mapper.StockLotMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
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
 * Integration tests for the {@link StockLotResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StockLotResourceIT {

    private static final String DEFAULT_BATCH_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BATCH_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_DATE = LocalDate.parse("2023-12-18");

    private static final Integer DEFAULT_QUANTITY_ON_HAND = 0;
    private static final Integer UPDATED_QUANTITY_ON_HAND = 1;

    private static final LocalDate DEFAULT_RECEIVED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECEIVED_DATE = LocalDate.parse("2023-12-18");

    private static final String ENTITY_API_URL = "/api/stock-lots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StockLotRepository stockLotRepository;

    @Mock
    private StockLotRepository stockLotRepositoryMock;

    @Autowired
    private StockLotMapper stockLotMapper;

    @Mock
    private StockLotService stockLotServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockLotMockMvc;

    private StockLot stockLot;

    private StockLot insertedStockLot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockLot createEntity(EntityManager em) {
        StockLot stockLot = new StockLot()
            .batchNumber(DEFAULT_BATCH_NUMBER)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .quantityOnHand(DEFAULT_QUANTITY_ON_HAND)
            .receivedDate(DEFAULT_RECEIVED_DATE);
        // Add required entity
        Medicine medicine;
        if (TestUtil.findAll(em, Medicine.class).isEmpty()) {
            medicine = MedicineResourceIT.createEntity(em);
            em.persist(medicine);
            em.flush();
        } else {
            medicine = TestUtil.findAll(em, Medicine.class).getFirst();
        }
        stockLot.setMedicine(medicine);
        // Add required entity
        StorageLocation storageLocation;
        if (TestUtil.findAll(em, StorageLocation.class).isEmpty()) {
            storageLocation = StorageLocationResourceIT.createEntity();
            em.persist(storageLocation);
            em.flush();
        } else {
            storageLocation = TestUtil.findAll(em, StorageLocation.class).getFirst();
        }
        stockLot.setStorageLocation(storageLocation);
        return stockLot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockLot createUpdatedEntity(EntityManager em) {
        StockLot updatedStockLot = new StockLot()
            .batchNumber(UPDATED_BATCH_NUMBER)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .quantityOnHand(UPDATED_QUANTITY_ON_HAND)
            .receivedDate(UPDATED_RECEIVED_DATE);
        // Add required entity
        Medicine medicine;
        if (TestUtil.findAll(em, Medicine.class).isEmpty()) {
            medicine = MedicineResourceIT.createUpdatedEntity(em);
            em.persist(medicine);
            em.flush();
        } else {
            medicine = TestUtil.findAll(em, Medicine.class).getFirst();
        }
        updatedStockLot.setMedicine(medicine);
        // Add required entity
        StorageLocation storageLocation;
        if (TestUtil.findAll(em, StorageLocation.class).isEmpty()) {
            storageLocation = StorageLocationResourceIT.createUpdatedEntity();
            em.persist(storageLocation);
            em.flush();
        } else {
            storageLocation = TestUtil.findAll(em, StorageLocation.class).getFirst();
        }
        updatedStockLot.setStorageLocation(storageLocation);
        return updatedStockLot;
    }

    @BeforeEach
    void initTest() {
        stockLot = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedStockLot != null) {
            stockLotRepository.delete(insertedStockLot);
            insertedStockLot = null;
        }
    }

    @Test
    @Transactional
    void createStockLot() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StockLot
        StockLotDTO stockLotDTO = stockLotMapper.toDto(stockLot);
        var returnedStockLotDTO = om.readValue(
            restStockLotMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockLotDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StockLotDTO.class
        );

        // Validate the StockLot in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStockLot = stockLotMapper.toEntity(returnedStockLotDTO);
        assertStockLotUpdatableFieldsEquals(returnedStockLot, getPersistedStockLot(returnedStockLot));

        insertedStockLot = returnedStockLot;
    }

    @Test
    @Transactional
    void createStockLotWithExistingId() throws Exception {
        // Create the StockLot with an existing ID
        stockLot.setId(1L);
        StockLotDTO stockLotDTO = stockLotMapper.toDto(stockLot);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockLotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockLotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockLot in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBatchNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockLot.setBatchNumber(null);

        // Create the StockLot, which fails.
        StockLotDTO stockLotDTO = stockLotMapper.toDto(stockLot);

        restStockLotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockLotDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpiryDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockLot.setExpiryDate(null);

        // Create the StockLot, which fails.
        StockLotDTO stockLotDTO = stockLotMapper.toDto(stockLot);

        restStockLotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockLotDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityOnHandIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockLot.setQuantityOnHand(null);

        // Create the StockLot, which fails.
        StockLotDTO stockLotDTO = stockLotMapper.toDto(stockLot);

        restStockLotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockLotDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReceivedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockLot.setReceivedDate(null);

        // Create the StockLot, which fails.
        StockLotDTO stockLotDTO = stockLotMapper.toDto(stockLot);

        restStockLotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockLotDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStockLots() throws Exception {
        // Initialize the database
        insertedStockLot = stockLotRepository.saveAndFlush(stockLot);

        // Get all the stockLotList
        restStockLotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockLot.getId().intValue())))
            .andExpect(jsonPath("$.[*].batchNumber").value(hasItem(DEFAULT_BATCH_NUMBER)))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantityOnHand").value(hasItem(DEFAULT_QUANTITY_ON_HAND)))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStockLotsWithEagerRelationshipsIsEnabled() throws Exception {
        when(stockLotServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStockLotMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(stockLotServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStockLotsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(stockLotServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStockLotMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(stockLotRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStockLot() throws Exception {
        // Initialize the database
        insertedStockLot = stockLotRepository.saveAndFlush(stockLot);

        // Get the stockLot
        restStockLotMockMvc
            .perform(get(ENTITY_API_URL_ID, stockLot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stockLot.getId().intValue()))
            .andExpect(jsonPath("$.batchNumber").value(DEFAULT_BATCH_NUMBER))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.quantityOnHand").value(DEFAULT_QUANTITY_ON_HAND))
            .andExpect(jsonPath("$.receivedDate").value(DEFAULT_RECEIVED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStockLot() throws Exception {
        // Get the stockLot
        restStockLotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStockLot() throws Exception {
        // Initialize the database
        insertedStockLot = stockLotRepository.saveAndFlush(stockLot);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockLot
        StockLot updatedStockLot = stockLotRepository.findById(stockLot.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStockLot are not directly saved in db
        em.detach(updatedStockLot);
        updatedStockLot
            .batchNumber(UPDATED_BATCH_NUMBER)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .quantityOnHand(UPDATED_QUANTITY_ON_HAND)
            .receivedDate(UPDATED_RECEIVED_DATE);
        StockLotDTO stockLotDTO = stockLotMapper.toDto(updatedStockLot);

        restStockLotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockLotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockLotDTO))
            )
            .andExpect(status().isOk());

        // Validate the StockLot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStockLotToMatchAllProperties(updatedStockLot);
    }

    @Test
    @Transactional
    void putNonExistingStockLot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockLot.setId(longCount.incrementAndGet());

        // Create the StockLot
        StockLotDTO stockLotDTO = stockLotMapper.toDto(stockLot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockLotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockLotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockLotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockLot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStockLot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockLot.setId(longCount.incrementAndGet());

        // Create the StockLot
        StockLotDTO stockLotDTO = stockLotMapper.toDto(stockLot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockLotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockLotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockLot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStockLot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockLot.setId(longCount.incrementAndGet());

        // Create the StockLot
        StockLotDTO stockLotDTO = stockLotMapper.toDto(stockLot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockLotMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockLotDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockLot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockLotWithPatch() throws Exception {
        // Initialize the database
        insertedStockLot = stockLotRepository.saveAndFlush(stockLot);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockLot using partial update
        StockLot partialUpdatedStockLot = new StockLot();
        partialUpdatedStockLot.setId(stockLot.getId());

        restStockLotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockLot.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStockLot))
            )
            .andExpect(status().isOk());

        // Validate the StockLot in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStockLotUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedStockLot, stockLot), getPersistedStockLot(stockLot));
    }

    @Test
    @Transactional
    void fullUpdateStockLotWithPatch() throws Exception {
        // Initialize the database
        insertedStockLot = stockLotRepository.saveAndFlush(stockLot);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockLot using partial update
        StockLot partialUpdatedStockLot = new StockLot();
        partialUpdatedStockLot.setId(stockLot.getId());

        partialUpdatedStockLot
            .batchNumber(UPDATED_BATCH_NUMBER)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .quantityOnHand(UPDATED_QUANTITY_ON_HAND)
            .receivedDate(UPDATED_RECEIVED_DATE);

        restStockLotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockLot.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStockLot))
            )
            .andExpect(status().isOk());

        // Validate the StockLot in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStockLotUpdatableFieldsEquals(partialUpdatedStockLot, getPersistedStockLot(partialUpdatedStockLot));
    }

    @Test
    @Transactional
    void patchNonExistingStockLot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockLot.setId(longCount.incrementAndGet());

        // Create the StockLot
        StockLotDTO stockLotDTO = stockLotMapper.toDto(stockLot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockLotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockLotDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stockLotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockLot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStockLot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockLot.setId(longCount.incrementAndGet());

        // Create the StockLot
        StockLotDTO stockLotDTO = stockLotMapper.toDto(stockLot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockLotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stockLotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockLot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStockLot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockLot.setId(longCount.incrementAndGet());

        // Create the StockLot
        StockLotDTO stockLotDTO = stockLotMapper.toDto(stockLot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockLotMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(stockLotDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockLot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStockLot() throws Exception {
        // Initialize the database
        insertedStockLot = stockLotRepository.saveAndFlush(stockLot);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the stockLot
        restStockLotMockMvc
            .perform(delete(ENTITY_API_URL_ID, stockLot.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return stockLotRepository.count();
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

    protected StockLot getPersistedStockLot(StockLot stockLot) {
        return stockLotRepository.findById(stockLot.getId()).orElseThrow();
    }

    protected void assertPersistedStockLotToMatchAllProperties(StockLot expectedStockLot) {
        assertStockLotAllPropertiesEquals(expectedStockLot, getPersistedStockLot(expectedStockLot));
    }

    protected void assertPersistedStockLotToMatchUpdatableProperties(StockLot expectedStockLot) {
        assertStockLotAllUpdatablePropertiesEquals(expectedStockLot, getPersistedStockLot(expectedStockLot));
    }
}
