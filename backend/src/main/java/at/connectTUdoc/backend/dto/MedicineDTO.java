package at.connectTUdoc.backend.dto;

import java.util.Objects;

/**
 * This class represents the layer between the database and the service layer and transforms the model object to an DTO and vise versa.
 */
public class MedicineDTO extends AbstractDTO {
    private String id;
    private String name;

    public MedicineDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MedicineDTO medicine = (MedicineDTO) obj;
        return Objects.equals(getId(), medicine.getId()) &&
                Objects.equals(getName(), medicine.getName());
    }
}
