package com.gilead.medicalinventory.repository;

import com.gilead.medicalinventory.domain.StorageLocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StorageLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StorageLocationRepository extends JpaRepository<StorageLocation, Long> {}
