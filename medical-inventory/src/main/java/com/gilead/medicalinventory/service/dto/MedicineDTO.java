package com.gilead.medicalinventory.service.dto;

import com.gilead.medicalinventory.domain.enumeration.DosageForm;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.gilead.medicalinventory.domain.Medicine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicineDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 120)
    private String name;

    @NotNull
    @Size(min = 2, max = 40)
    private String sku;

    @Size(max = 120)
    private String genericName;

    @NotNull
    private DosageForm dosageForm;

    @Size(max = 40)
    private String strength;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal unitPrice;

    @NotNull
    @Min(value = 0)
    private Integer reorderLevel;

    @NotNull
    private Boolean controlledSubstance;

    @Lob
    private String description;

    @NotNull
    private CategoryDTO category;

    private SupplierDTO supplier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public DosageForm getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(DosageForm dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public Boolean getControlledSubstance() {
        return controlledSubstance;
    }

    public void setControlledSubstance(Boolean controlledSubstance) {
        this.controlledSubstance = controlledSubstance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public SupplierDTO getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierDTO supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicineDTO)) {
            return false;
        }

        MedicineDTO medicineDTO = (MedicineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicineDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sku='" + getSku() + "'" +
            ", genericName='" + getGenericName() + "'" +
            ", dosageForm='" + getDosageForm() + "'" +
            ", strength='" + getStrength() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", reorderLevel=" + getReorderLevel() +
            ", controlledSubstance='" + getControlledSubstance() + "'" +
            ", description='" + getDescription() + "'" +
            ", category=" + getCategory() +
            ", supplier=" + getSupplier() +
            "}";
    }
}
