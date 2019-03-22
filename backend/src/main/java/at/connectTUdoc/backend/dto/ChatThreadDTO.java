package at.connectTUdoc.backend.dto;

import java.util.Objects;

/**
 * This class represents the layer between the database and the service layer and transforms the model object to an DTO and vise versa.
 */
public class ChatThreadDTO extends AbstractDTO{

    private Long id;
    private PatientDTO patient;
    private OfficeDTO office;
    private int unreadMessages;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatThreadDTO that = (ChatThreadDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(patient.getId(), that.patient.getId()) &&
                Objects.equals(office.getId(), that.office.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patient.getId(), office.getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    public OfficeDTO getOffice() {
        return office;
    }

    public void setOffice(OfficeDTO office) {
        this.office = office;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }
}
