package com.gilead.medicalinventory.service;

import com.gilead.medicalinventory.service.dto.StockLotDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.gilead.medicalinventory.domain.StockLot}.
 */
public interface StockLotService {
    /**
     * Save a stockLot.
     *
     * @param stockLotDTO the entity to save.
     * @return the persisted entity.
     */
    StockLotDTO save(StockLotDTO stockLotDTO);

    /**
     * Updates a stockLot.
     *
     * @param stockLotDTO the entity to update.
     * @return the persisted entity.
     */
    StockLotDTO update(StockLotDTO stockLotDTO);

    /**
     * Partially updates a stockLot.
     *
     * @param stockLotDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StockLotDTO> partialUpdate(StockLotDTO stockLotDTO);

    /**
     * Get all the stockLots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StockLotDTO> findAll(Pageable pageable);

    /**
     * Get all the stockLots with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StockLotDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" stockLot.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StockLotDTO> findOne(Long id);

    /**
     * Delete the "id" stockLot.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
