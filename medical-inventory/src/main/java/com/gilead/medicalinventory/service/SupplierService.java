package com.gilead.medicalinventory.service;

import com.gilead.medicalinventory.service.dto.SupplierDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.gilead.medicalinventory.domain.Supplier}.
 */
public interface SupplierService {
    /**
     * Save a supplier.
     *
     * @param supplierDTO the entity to save.
     * @return the persisted entity.
     */
    SupplierDTO save(SupplierDTO supplierDTO);

    /**
     * Updates a supplier.
     *
     * @param supplierDTO the entity to update.
     * @return the persisted entity.
     */
    SupplierDTO update(SupplierDTO supplierDTO);

    /**
     * Partially updates a supplier.
     *
     * @param supplierDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SupplierDTO> partialUpdate(SupplierDTO supplierDTO);

    /**
     * Get all the suppliers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SupplierDTO> findAll(Pageable pageable);

    /**
     * Get the "id" supplier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplierDTO> findOne(Long id);

    /**
     * Delete the "id" supplier.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
