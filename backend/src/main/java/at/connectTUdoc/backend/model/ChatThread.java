package at.connectTUdoc.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

/**
 * This class represents the sub-model of the database
 */
@Entity
public class ChatThread {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="patient_id")
    @JsonBackReference
    private Patient patient;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="office_id")
    @JsonBackReference
    private Office office;
    @Column(name="unreadMessages",columnDefinition = "int default 0")
    private int unreadMessages;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatThread that = (ChatThread) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(patient.getId(), that.patient.getId()) &&
                Objects.equals(office.getId(), that.office.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patient, office);
    }

    public ChatThread() {}

    public ChatThread(Patient patient, Office office) {
        this.patient = patient;
        this.office = office;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    @Override
    public String toString() {
        return "ChatThread{" +
                "id=" + id +
                ", Patient=" + patient.getId() +
                ", Office=" + office.getId() +
                '}';
    }
}
