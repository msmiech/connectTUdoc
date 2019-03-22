package at.connectTUdoc.backend.dto;

import at.connectTUdoc.backend.model.MedWorkerType;
import at.ws18_ase_qse_03.backend.model.*;

import java.util.List;
import java.util.Objects;

/**
 * This class represents the layer between the database and the service layer and transforms the model object to an DTO and vise versa.
 */
public class MedicalWorkerDTO extends PersonDTO{

    private List<SpecialityDTO> specialities;
    private MedWorkerType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MedicalWorkerDTO that = (MedicalWorkerDTO) o;
        return Objects.equals(specialities, that.specialities) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), specialities, type);
    }

    public List<SpecialityDTO> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(List<SpecialityDTO> specialities) {
        this.specialities = specialities;
    }

    public MedWorkerType getType() {
        return type;
    }

    public void setType(MedWorkerType type) {
        this.type = type;
    }


}
