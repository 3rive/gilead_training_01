package com.gilead.medicalinventory.service.dto;

import com.gilead.medicalinventory.domain.enumeration.MovementType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.gilead.medicalinventory.domain.StockMovement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockMovementDTO implements Serializable {

    private Long id;

    @NotNull
    private MovementType movementType;

    @NotNull
    private Integer quantity;

    @NotNull
    private Instant occurredAt;

    @Size(max = 255)
    private String reason;

    @Size(max = 40)
    private String referenceNumber;

    @NotNull
    private StockLotDTO stockLot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public StockLotDTO getStockLot() {
        return stockLot;
    }

    public void setStockLot(StockLotDTO stockLot) {
        this.stockLot = stockLot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockMovementDTO)) {
            return false;
        }

        StockMovementDTO stockMovementDTO = (StockMovementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stockMovementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockMovementDTO{" +
            "id=" + getId() +
            ", movementType='" + getMovementType() + "'" +
            ", quantity=" + getQuantity() +
            ", occurredAt='" + getOccurredAt() + "'" +
            ", reason='" + getReason() + "'" +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", stockLot=" + getStockLot() +
            "}";
    }
}
