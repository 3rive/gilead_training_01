package com.gilead.medicalinventory.service;

import com.gilead.medicalinventory.service.dto.StorageLocationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.gilead.medicalinventory.domain.StorageLocation}.
 */
public interface StorageLocationService {
    /**
     * Save a storageLocation.
     *
     * @param storageLocationDTO the entity to save.
     * @return the persisted entity.
     */
    StorageLocationDTO save(StorageLocationDTO storageLocationDTO);

    /**
     * Updates a storageLocation.
     *
     * @param storageLocationDTO the entity to update.
     * @return the persisted entity.
     */
    StorageLocationDTO update(StorageLocationDTO storageLocationDTO);

    /**
     * Partially updates a storageLocation.
     *
     * @param storageLocationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StorageLocationDTO> partialUpdate(StorageLocationDTO storageLocationDTO);

    /**
     * Get all the storageLocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StorageLocationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" storageLocation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StorageLocationDTO> findOne(Long id);

    /**
     * Delete the "id" storageLocation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
