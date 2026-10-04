package com.gilead.medicalinventory.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.gilead.medicalinventory.domain.StorageLocation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StorageLocationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    private String code;

    @NotNull
    @Size(min = 2, max = 80)
    private String name;

    @Size(max = 80)
    private String building;

    @NotNull
    private Boolean temperatureControlled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Boolean getTemperatureControlled() {
        return temperatureControlled;
    }

    public void setTemperatureControlled(Boolean temperatureControlled) {
        this.temperatureControlled = temperatureControlled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StorageLocationDTO)) {
            return false;
        }

        StorageLocationDTO storageLocationDTO = (StorageLocationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, storageLocationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StorageLocationDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", building='" + getBuilding() + "'" +
            ", temperatureControlled='" + getTemperatureControlled() + "'" +
            "}";
    }
}
