package at.connectTUdoc.backend.model;

import at.connectTUdoc.backend.helper.MedConnectConstants;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents the sub-model of the database
 */
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonBackReference
    private Office office;
    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient; // reference to patient
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
        var that = (Appointment) o;

        var officesEqual = false;
        if (ObjectUtils.isEmpty(office) && ObjectUtils.isEmpty(that.office)) {
            officesEqual = true;
        } else {
            officesEqual = Objects.equals(office.getId(), that.office.getId());
        }

        var patientsEqual = false;
        if (ObjectUtils.isEmpty(patient) && ObjectUtils.isEmpty(that.patient)) {
            patientsEqual = true;
        } else {
            patientsEqual = Objects.equals(patient.getId(), that.patient.getId());
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
        List<Object> hashObjects = new ArrayList();
        hashObjects.add(id);
        hashObjects.add(patientName);
        hashObjects.add(appointmentBegin);
        hashObjects.add(appointmentEnd);
        if (office != null) {
            hashObjects.add(office.getId());
        }
        if (patient != null) {
            hashObjects.add(patient.getId());
        }
        return Objects.hash(hashObjects.toArray());
    }


    public Appointment() {
    }

    public Appointment(Office office, Patient patient, LocalDateTime appointmentBegin, LocalDateTime appointmentEnd) {
        this.office = office;
        this.appointmentBegin = appointmentBegin;
        this.appointmentEnd = appointmentEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
