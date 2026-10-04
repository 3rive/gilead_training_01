package com.gilead.medicalinventory.service;

import com.gilead.medicalinventory.service.dto.MedicineDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.gilead.medicalinventory.domain.Medicine}.
 */
public interface MedicineService {
    /**
     * Save a medicine.
     *
     * @param medicineDTO the entity to save.
     * @return the persisted entity.
     */
    MedicineDTO save(MedicineDTO medicineDTO);

    /**
     * Updates a medicine.
     *
     * @param medicineDTO the entity to update.
     * @return the persisted entity.
     */
    MedicineDTO update(MedicineDTO medicineDTO);

    /**
     * Partially updates a medicine.
     *
     * @param medicineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MedicineDTO> partialUpdate(MedicineDTO medicineDTO);

    /**
     * Get all the medicines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MedicineDTO> findAll(Pageable pageable);

    /**
     * Get all the medicines with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MedicineDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" medicine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MedicineDTO> findOne(Long id);

    /**
     * Delete the "id" medicine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
