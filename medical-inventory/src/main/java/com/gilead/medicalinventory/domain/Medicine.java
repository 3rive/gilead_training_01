package com.gilead.medicalinventory.domain;

import com.gilead.medicalinventory.domain.enumeration.DosageForm;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Medicine.
 */
@Entity
@Table(name = "medicine")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medicine implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 120)
    @Column(name = "name", length = 120, nullable = false)
    private String name;

    @NotNull
    @Size(min = 2, max = 40)
    @Column(name = "sku", length = 40, nullable = false, unique = true)
    private String sku;

    @Size(max = 120)
    @Column(name = "generic_name", length = 120)
    private String genericName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "dosage_form", nullable = false)
    private DosageForm dosageForm;

    @Size(max = 40)
    @Column(name = "strength", length = 40)
    private String strength;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "unit_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @NotNull
    @Min(value = 0)
    @Column(name = "reorder_level", nullable = false)
    private Integer reorderLevel;

    @NotNull
    @Column(name = "controlled_substance", nullable = false)
    private Boolean controlledSubstance;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medicine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Medicine name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return this.sku;
    }

    public Medicine sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getGenericName() {
        return this.genericName;
    }

    public Medicine genericName(String genericName) {
        this.setGenericName(genericName);
        return this;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public DosageForm getDosageForm() {
        return this.dosageForm;
    }

    public Medicine dosageForm(DosageForm dosageForm) {
        this.setDosageForm(dosageForm);
        return this;
    }

    public void setDosageForm(DosageForm dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getStrength() {
        return this.strength;
    }

    public Medicine strength(String strength) {
        this.setStrength(strength);
        return this;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public Medicine unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getReorderLevel() {
        return this.reorderLevel;
    }

    public Medicine reorderLevel(Integer reorderLevel) {
        this.setReorderLevel(reorderLevel);
        return this;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public Boolean getControlledSubstance() {
        return this.controlledSubstance;
    }

    public Medicine controlledSubstance(Boolean controlledSubstance) {
        this.setControlledSubstance(controlledSubstance);
        return this;
    }

    public void setControlledSubstance(Boolean controlledSubstance) {
        this.controlledSubstance = controlledSubstance;
    }

    public String getDescription() {
        return this.description;
    }

    public Medicine description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Medicine category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Supplier getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Medicine supplier(Supplier supplier) {
        this.setSupplier(supplier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medicine)) {
            return false;
        }
        return getId() != null && getId().equals(((Medicine) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medicine{" +
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
            "}";
    }
}
