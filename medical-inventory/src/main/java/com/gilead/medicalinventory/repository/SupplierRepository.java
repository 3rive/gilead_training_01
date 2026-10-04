package com.gilead.medicalinventory.repository;

import com.gilead.medicalinventory.domain.Supplier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Supplier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {}
