package com.gilead.medicalinventory.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.gilead.medicalinventory.domain.StockLot} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockLotDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 40)
    private String batchNumber;

    @NotNull
    private LocalDate expiryDate;

    @NotNull
    @Min(value = 0)
    private Integer quantityOnHand;

    @NotNull
    private LocalDate receivedDate;

    @NotNull
    private MedicineDTO medicine;

    @NotNull
    private StorageLocationDTO storageLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(Integer quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public MedicineDTO getMedicine() {
        return medicine;
    }

    public void setMedicine(MedicineDTO medicine) {
        this.medicine = medicine;
    }

    public StorageLocationDTO getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocationDTO storageLocation) {
        this.storageLocation = storageLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockLotDTO)) {
            return false;
        }

        StockLotDTO stockLotDTO = (StockLotDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stockLotDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockLotDTO{" +
            "id=" + getId() +
            ", batchNumber='" + getBatchNumber() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", quantityOnHand=" + getQuantityOnHand() +
            ", receivedDate='" + getReceivedDate() + "'" +
            ", medicine=" + getMedicine() +
            ", storageLocation=" + getStorageLocation() +
            "}";
    }
}
