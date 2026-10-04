package com.gilead.medicalinventory.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StorageLocation.
 */
@Entity
@Table(name = "storage_location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StorageLocation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "code", length = 20, nullable = false, unique = true)
    private String code;

    @NotNull
    @Size(min = 2, max = 80)
    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @Size(max = 80)
    @Column(name = "building", length = 80)
    private String building;

    @NotNull
    @Column(name = "temperature_controlled", nullable = false)
    private Boolean temperatureControlled;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StorageLocation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public StorageLocation code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public StorageLocation name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuilding() {
        return this.building;
    }

    public StorageLocation building(String building) {
        this.setBuilding(building);
        return this;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Boolean getTemperatureControlled() {
        return this.temperatureControlled;
    }

    public StorageLocation temperatureControlled(Boolean temperatureControlled) {
        this.setTemperatureControlled(temperatureControlled);
        return this;
    }

    public void setTemperatureControlled(Boolean temperatureControlled) {
        this.temperatureControlled = temperatureControlled;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StorageLocation)) {
            return false;
        }
        return getId() != null && getId().equals(((StorageLocation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StorageLocation{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", building='" + getBuilding() + "'" +
            ", temperatureControlled='" + getTemperatureControlled() + "'" +
            "}";
    }
}
