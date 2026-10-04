package com.gilead.medicalinventory.repository;

import com.gilead.medicalinventory.domain.Medicine;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Medicine entity.
 */
@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    default Optional<Medicine> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Medicine> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Medicine> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select medicine from Medicine medicine left join fetch medicine.category left join fetch medicine.supplier",
        countQuery = "select count(medicine) from Medicine medicine"
    )
    Page<Medicine> findAllWithToOneRelationships(Pageable pageable);

    @Query("select medicine from Medicine medicine left join fetch medicine.category left join fetch medicine.supplier")
    List<Medicine> findAllWithToOneRelationships();

    @Query(
        "select medicine from Medicine medicine left join fetch medicine.category left join fetch medicine.supplier where medicine.id =:id"
    )
    Optional<Medicine> findOneWithToOneRelationships(@Param("id") Long id);
}
