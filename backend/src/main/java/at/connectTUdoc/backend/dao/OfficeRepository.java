package at.connectTUdoc.backend.dao;

import at.connectTUdoc.backend.model.MedicalWorker;
import at.connectTUdoc.backend.model.Office;
import at.connectTUdoc.backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This class represents the queries for the service layer to interact with the database.
 */
@Repository
public interface OfficeRepository extends JpaRepository<Office, Long>
{
    /**
     * @param oName - the name of the office
     * @return a list of offices which contains exact the name
     */
    List<Office> findOfficesByName(String oName);

    /**
     * @param name - the name of the office
     * @return a list of offices which contains exact the name ignoring the case sibling
     */
    List<Office> findOfficesByNameIgnoreCaseContaining(String name);

    /**
     * @param medWorkers - a list of office workers
     * @return a list of offices which contains the office workers
     */
    List<Office> findOfficesByOfficeWorkersIn(List<MedicalWorker> medWorkers);

    /**
     * @param medicalWorker - the medical worker assigned to an office
     * @return the office of the medical worker assigned
     */
    Optional<Office> findOfficeByOfficeWorkersIn(MedicalWorker medicalWorker);

    /**
     * @param patient - the patient object
     * @return a list of offices where the patient is registered
     */
    List<Office> findOfficesByOfficePatientsIn(Patient patient);

}
