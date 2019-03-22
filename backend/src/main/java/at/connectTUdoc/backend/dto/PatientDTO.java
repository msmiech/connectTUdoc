package at.connectTUdoc.backend.dto;

import java.util.Objects;

/**
 * This class represents the layer between the database and the service layer and transforms the model object to an DTO and vise versa.
 */
public class PatientDTO extends PersonDTO {

    private String svnr;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PatientDTO that = (PatientDTO) o;
        return Objects.equals(svnr, that.svnr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), svnr);
    }


    public String getSvnr() {
        return svnr;
    }

    public void setSvnr(String svnr) {
        this.svnr = svnr;
    }

}
