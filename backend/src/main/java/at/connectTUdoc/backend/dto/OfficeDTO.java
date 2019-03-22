package at.connectTUdoc.backend.dto;

import at.connectTUdoc.backend.util.DataGeneration;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents the layer between the database and the service layer and transforms the model object to an DTO and vise versa.
 */
public class OfficeDTO extends AbstractDTO {

    private Long id;
    private String name;
    private String phone;
    private String fax;
    private AddressDTO address;
    private String email;
    private List<OfficeHourDTO> officehours;
    @JsonIgnore
    private List<AppointmentDTO> appointments;
    private List<MedicalWorkerDTO> officeWorkers;
    @JsonIgnore
    private List<PatientDTO> officePatients;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfficeDTO officeDTO = (OfficeDTO) o;

        boolean appointmentsEquals = false;
        if (ObjectUtils.isEmpty(appointments) && ObjectUtils.isEmpty(officeDTO.appointments)) {
            appointmentsEquals = true;
        } else {
            appointmentsEquals = Objects.equals(appointments, officeDTO.appointments);
        }

        return Objects.equals(id, officeDTO.id) &&
                Objects.equals(name, officeDTO.name) &&
                Objects.equals(phone, officeDTO.phone) &&
                Objects.equals(fax, officeDTO.fax) &&
                Objects.equals(address, officeDTO.address) &&
                Objects.equals(email, officeDTO.email) &&
                Objects.equals(officehours, officeDTO.officehours) &&
                appointmentsEquals &&
                Objects.equals(officeWorkers, officeDTO.officeWorkers) &&
                Objects.equals(officePatients,officeDTO.officePatients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone, fax, address, email, officehours, officeWorkers, officePatients);
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

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<OfficeHourDTO> getOfficehours() {
        DataGeneration.sortByWeekDays(this.officehours);
        return this.officehours;
    }

    public void setOfficehours(List<OfficeHourDTO> officehours) {
        this.officehours = Objects.requireNonNullElseGet(officehours, ArrayList::new);
    }

    public List<AppointmentDTO> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentDTO> appointments) {
        this.appointments = Objects.requireNonNullElseGet(appointments, ArrayList::new);
    }

    public List<MedicalWorkerDTO> getOfficeWorkers() {
        return officeWorkers;
    }

    public void setOfficeWorkers(List<MedicalWorkerDTO> officeWorkers) {
        this.officeWorkers = Objects.requireNonNullElseGet(officeWorkers, ArrayList::new);
    }

    public List<PatientDTO> getOfficePatients() {
        return officePatients;
    }

    public void setOfficePatients(List<PatientDTO> officePatients) {
        this.officePatients = officePatients;
    }
}
