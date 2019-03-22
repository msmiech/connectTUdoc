package at.connectTUdoc.backend.dao;

import at.connectTUdoc.backend.model.Office;
import at.connectTUdoc.backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class represents the queries for the service layer to interact with the database.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * @param uid - the uid of the patient
     * @return finds a patient object with the uid
     */
    Patient findPatientByUid(String uid);

    /**
     * Deletes an patient with the uid
     * @param uid - the uid of the patient to delete
     */
    void deletePatientByUid(String uid);

    /**
     * @param uid - the uid of the patient to search
     * @return true/false regarding the search result
     */
    boolean existsByUid(String uid);

    /**
     * @param office - the office to search for
     * @return a list of patients where patients are not confirmed
     */
    @Query("Select p from at.ws18_ase_qse_03.backend.model.Patient p where exists (Select a from Appointment a where a.office = :office AND a.patient = p) AND EXISTS (Select o from Office o where o = :office AND p not member of o.officePatients) ORDER BY p.lastName, p.firstName")
    List<Patient> getUnconfirmedPatients(@Param("office") Office office);

}
