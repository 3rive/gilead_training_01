package com.gilead.medicalinventory.repository;

import com.gilead.medicalinventory.domain.StockLot;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StockLot entity.
 */
@Repository
public interface StockLotRepository extends JpaRepository<StockLot, Long> {
    default Optional<StockLot> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<StockLot> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<StockLot> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select stockLot from StockLot stockLot left join fetch stockLot.medicine left join fetch stockLot.storageLocation",
        countQuery = "select count(stockLot) from StockLot stockLot"
    )
    Page<StockLot> findAllWithToOneRelationships(Pageable pageable);

    @Query("select stockLot from StockLot stockLot left join fetch stockLot.medicine left join fetch stockLot.storageLocation")
    List<StockLot> findAllWithToOneRelationships();

    @Query(
        "select stockLot from StockLot stockLot left join fetch stockLot.medicine left join fetch stockLot.storageLocation where stockLot.id =:id"
    )
    Optional<StockLot> findOneWithToOneRelationships(@Param("id") Long id);
}
