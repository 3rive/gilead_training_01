package com.gilead.medicalinventory.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StockLot.
 */
@Entity
@Table(name = "stock_lot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockLot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "batch_number", length = 40, nullable = false)
    private String batchNumber;

    @NotNull
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @NotNull
    @Min(value = 0)
    @Column(name = "quantity_on_hand", nullable = false)
    private Integer quantityOnHand;

    @NotNull
    @Column(name = "received_date", nullable = false)
    private LocalDate receivedDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "category", "supplier" }, allowSetters = true)
    private Medicine medicine;

    @ManyToOne(optional = false)
    @NotNull
    private StorageLocation storageLocation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StockLot id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchNumber() {
        return this.batchNumber;
    }

    public StockLot batchNumber(String batchNumber) {
        this.setBatchNumber(batchNumber);
        return this;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public LocalDate getExpiryDate() {
        return this.expiryDate;
    }

    public StockLot expiryDate(LocalDate expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getQuantityOnHand() {
        return this.quantityOnHand;
    }

    public StockLot quantityOnHand(Integer quantityOnHand) {
        this.setQuantityOnHand(quantityOnHand);
        return this;
    }

    public void setQuantityOnHand(Integer quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public LocalDate getReceivedDate() {
        return this.receivedDate;
    }

    public StockLot receivedDate(LocalDate receivedDate) {
        this.setReceivedDate(receivedDate);
        return this;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Medicine getMedicine() {
        return this.medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public StockLot medicine(Medicine medicine) {
        this.setMedicine(medicine);
        return this;
    }

    public StorageLocation getStorageLocation() {
        return this.storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public StockLot storageLocation(StorageLocation storageLocation) {
        this.setStorageLocation(storageLocation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockLot)) {
            return false;
        }
        return getId() != null && getId().equals(((StockLot) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockLot{" +
            "id=" + getId() +
            ", batchNumber='" + getBatchNumber() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", quantityOnHand=" + getQuantityOnHand() +
            ", receivedDate='" + getReceivedDate() + "'" +
            "}";
    }
}
