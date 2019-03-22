package at.connectTUdoc.backend.service;

import at.connectTUdoc.backend.dto.MedicalWorkerDTO;
import at.connectTUdoc.backend.dto.OfficeDTO;
import at.connectTUdoc.backend.dto.PatientDTO;
import at.connectTUdoc.backend.dto.RegistrationCodeDTO;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.List;

/**
 * This class represents all methods for operating with an office.
 */
public interface OfficeService {

    /**
     * @return a list of offices
     */
    List<OfficeDTO> findAllOffices();

    /***
     * @param name - the name of the office(s)
     * @return a list of offices which have exactly the name to find
     * @throws ResourceNotFoundException - if no entry was found
     */
    List<OfficeDTO> findOfficesByName(String name) throws ResourceNotFoundException;

    /**
     * @param id - the id of the office
     * @return maybe a list of offices which have the id
     * @throws ResourceNotFoundException - if no entry was found
     */
    OfficeDTO findOfficeById(long id) throws ResourceNotFoundException;

    /**
     *
     * @param office - thhe new office object to store in the database
     * @return the office object with the id of the table
     */
    OfficeDTO createOffice(OfficeDTO office);

    /**
     * @param id - the id of the office to delete
     */
    void deleteOfficeById(long id);

    /**
     * @param office - the office to update
     * @return the updated office object
     */
    OfficeDTO updateOffice(OfficeDTO office);

    /**
     * clears the database from each office entry
     */
    void deleteAllOffices();

    /**
     * @param medicalWorker - the medical worker assigned to an office
     * @return the office of the medical worker
     * @throws ResourceNotFoundException - if no entry was found
     */
    OfficeDTO findOfficeByMedicalWorker(MedicalWorkerDTO medicalWorker) throws ResourceNotFoundException;

    /**
     * Retrieve all offices where a patient is client
     *
     * @param patient - the patient assigned to an office
     * @return the offices of the patient
     */
    List<OfficeDTO> getOfficesByPatient(PatientDTO patient);

    List<OfficeDTO> getOfficeBySearchTextDoctorNameOnly(String searchText);

    List<OfficeDTO> getOfficeBySearchTextOfficeNameOnly(String searchText);

    List<OfficeDTO> getOfficeBySearchTextSpecialityNameOnly(String searchText);

    List<PatientDTO> getPatientsByOfficeId(long id);

    /**
     * Retrieves all patients that have appointments for the given office but are not registered at the office.
     * @param id office if for which unconfirmed patients are retrieved
     * @return list of unconfirmed patients
     */
    List<PatientDTO> getUnconfirmedPatients(Long id);

    /**
     * Adds the patient to the office.
     * @param officeId id of the office to which the patient is added
     * @param patientId id of the patient which is added to the office
     * @param code registration code to be verified
     */
    void addPatientToOffice(Long officeId, Long patientId, RegistrationCodeDTO code);

    /**
     * Deletes all appointments by the given office and patient.
     * @param officeId id of the office to identify appointments
     * @param patientId if of the patient to identify appointments
     */
    void deleteAppointmentsByOfficeANDPatient(Long officeId, Long patientId);

    /**
     * The method creates a new registration code for the given patient.
     * The new code is valid for 5 minutes.
     * @param patientId id of the patient for which the code is generated
     * @return returns the new registration code
     */
    RegistrationCodeDTO getRegistrationCode(Long patientId);
}
