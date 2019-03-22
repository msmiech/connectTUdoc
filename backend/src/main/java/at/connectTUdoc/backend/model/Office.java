package at.connectTUdoc.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents the sub-model of the database
 */
@Entity
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String fax;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
    @Column
    private String email;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            targetEntity = OfficeHour.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "office_id")
    @JsonManagedReference
    private List<OfficeHour> officehours;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            targetEntity = Appointment.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "office_id")
    @JsonManagedReference
    private List<Appointment> appointments;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "OfficeWorker",
            joinColumns = @JoinColumn(name = "officeID", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "medicalWorkerID", referencedColumnName = "id"))
    @JsonManagedReference
    private List<MedicalWorker> officeWorkers;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "OfficePatient",
            joinColumns = @JoinColumn(name = "officeID", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "patientID", referencedColumnName = "id"))
    @JsonManagedReference
    private List<Patient> officePatients;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Office office = (Office) o;


        boolean appointmentsEquals = false;
        if (ObjectUtils.isEmpty(appointments) && ObjectUtils.isEmpty(office.appointments)) {
            appointmentsEquals = true;
        } else {
            appointmentsEquals = Objects.equals(appointments, office.appointments);
        }

        return Objects.equals(id, office.id) &&
                Objects.equals(name, office.name) &&
                Objects.equals(phone, office.phone) &&
                Objects.equals(fax, office.fax) &&
                Objects.equals(address, office.address) &&
                Objects.equals(email, office.email) &&
                Objects.equals(officehours, office.officehours) &&
                appointmentsEquals &&
                Objects.equals(officePatients, office.officePatients) &&
                Objects.equals(officeWorkers, office.officeWorkers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone, fax, address, email, officehours, appointments, officeWorkers, officePatients);
    }

    public Office() {
    }

    public Office(String name, String phone, String fax, Address address, String email) {
        this.name = name;
        this.phone = phone;
        this.fax = fax;
        this.address = address;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<OfficeHour> getOfficehours() {
        return officehours;
    }

    public void setOfficeHours(List<OfficeHour> officehours) {
        this.officehours = officehours;
    }

    public List<MedicalWorker> getOfficeWorkers() {
        return officeWorkers;
    }

    public void setOfficeWorkers(List<MedicalWorker> officeWorkers) {
        this.officeWorkers = officeWorkers;
    }

    public List<Patient> getOfficePatients() {
        return officePatients;
    }

    public void setOfficePatients(List<Patient> officePatients) {
        this.officePatients = officePatients;
    }

    public void addOfficePatient(Patient patient) {
        if(this.officePatients == null) {
            this.officePatients = new ArrayList<>();
        }
        this.officePatients.add(patient);
    }
}
