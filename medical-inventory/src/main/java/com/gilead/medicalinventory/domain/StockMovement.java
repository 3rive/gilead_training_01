package com.gilead.medicalinventory.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gilead.medicalinventory.domain.enumeration.MovementType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StockMovement.
 */
@Entity
@Table(name = "stock_movement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockMovement implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Size(max = 255)
    @Column(name = "reason", length = 255)
    private String reason;

    @Size(max = 40)
    @Column(name = "reference_number", length = 40)
    private String referenceNumber;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "medicine", "storageLocation" }, allowSetters = true)
    private StockLot stockLot;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StockMovement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovementType getMovementType() {
        return this.movementType;
    }

    public StockMovement movementType(MovementType movementType) {
        this.setMovementType(movementType);
        return this;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public StockMovement quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instant getOccurredAt() {
        return this.occurredAt;
    }

    public StockMovement occurredAt(Instant occurredAt) {
        this.setOccurredAt(occurredAt);
        return this;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }

    public String getReason() {
        return this.reason;
    }

    public StockMovement reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReferenceNumber() {
        return this.referenceNumber;
    }

    public StockMovement referenceNumber(String referenceNumber) {
        this.setReferenceNumber(referenceNumber);
        return this;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public StockLot getStockLot() {
        return this.stockLot;
    }

    public void setStockLot(StockLot stockLot) {
        this.stockLot = stockLot;
    }

    public StockMovement stockLot(StockLot stockLot) {
        this.setStockLot(stockLot);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockMovement)) {
            return false;
        }
        return getId() != null && getId().equals(((StockMovement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockMovement{" +
            "id=" + getId() +
            ", movementType='" + getMovementType() + "'" +
            ", quantity=" + getQuantity() +
            ", occurredAt='" + getOccurredAt() + "'" +
            ", reason='" + getReason() + "'" +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            "}";
    }
}
