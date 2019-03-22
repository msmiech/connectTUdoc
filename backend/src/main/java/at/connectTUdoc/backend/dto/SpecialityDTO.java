package at.connectTUdoc.backend.dto;

import java.util.Objects;

/**
 * This class represents the layer between the database and the service layer and transforms the model object to an DTO and vise versa.
 */
public class SpecialityDTO extends AbstractDTO{

    private Long id;
    private String specialityName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecialityDTO that = (SpecialityDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(specialityName, that.specialityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, specialityName);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }
}
