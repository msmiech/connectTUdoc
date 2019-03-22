package at.connectTUdoc.backend.dto;

import at.connectTUdoc.backend.helper.MedConnectConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class represents the layer between the database and the service layer and transforms the model object to an DTO and vise versa.
 */
public class AppointmentDTO extends AbstractDTO {

    private Long id;
    private OfficeDTO office;
    private PatientDTO patient;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = MedConnectConstants.JSON_FORMAT_LOCALDATETIME)
    private LocalDateTime appointmentBegin;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = MedConnectConstants.JSON_FORMAT_LOCALDATETIME)
    private LocalDateTime appointmentEnd;
    private String patientName;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentDTO that = (AppointmentDTO) o;

        var officesEqual = false;
        if (office != null && that.office != null) {
            officesEqual = Objects.equals(office.getId(), that.office.getId());
        } else if (office == null && that.office == null) {
            officesEqual = true;
        }

        var patientsEqual = false;
        if (patient != null && that.patient != null) {
            patientsEqual = Objects.equals(patient.getId(), that.patient.getId());
        } else if (patient == null && that.patient == null) {
            patientsEqual = true;
        }

        return Objects.equals(id, that.id) &&
                officesEqual &&
                patientsEqual &&
                Objects.equals(patientName,that.patientName) &&
                Objects.equals(appointmentBegin, that.appointmentBegin) &&
                Objects.equals(appointmentEnd, that.appointmentEnd);
    }

    @Override
    public int hashCode() {
        List<Object> hashObjects = new ArrayList<>(Arrays.asList(id, appointmentBegin, appointmentEnd));
        if (office != null) {
            hashObjects.add(office.getId());
        }
        if (patient != null) {
            hashObjects.add(patient.getId());
        }
        return Objects.hash(hashObjects.toArray());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OfficeDTO getOffice() {
        return office;
    }

    public void setOffice(OfficeDTO office) {
        this.office = office;
    }

    public LocalDateTime getAppointmentBegin() {
        return appointmentBegin;
    }

    public void setAppointmentBegin(LocalDateTime appointmentBegin) {
        this.appointmentBegin = appointmentBegin;
    }

    public LocalDateTime getAppointmentEnd() {
        return appointmentEnd;
    }

    public void setAppointmentEnd(LocalDateTime appointmentEnd) {
        this.appointmentEnd = appointmentEnd;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
