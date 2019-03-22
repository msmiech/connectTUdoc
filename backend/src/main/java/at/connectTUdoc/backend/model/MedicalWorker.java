package at.connectTUdoc.backend.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * This class represents the sub-model of the database
 */
@Entity(name = "medicalworker")
public class MedicalWorker extends Person {
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "DoctorSpeciality",
            joinColumns = @JoinColumn(name = "doctorID", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "specialityID", referencedColumnName = "id"))
    private List<Speciality> specialities;
    private MedWorkerType type;
    @OneToMany(
            mappedBy = "medicalWorker",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalWorker that = (MedicalWorker) o;
        return super.equals(that) && Objects.equals(specialities, that.specialities) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),specialities, type);
    }

    public List<Speciality> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(List<Speciality> specialities) {
        this.specialities = specialities;
    }

    public MedWorkerType getType() {
        return type;
    }

    public void setType(MedWorkerType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MedicalWorker{" +
                "specialities=" + specialities +
                ", type=" + type +
                '}';
    }
}
