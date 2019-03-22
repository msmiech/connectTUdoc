package at.connectTUdoc.backend.service;

import at.connectTUdoc.backend.dto.PatientDTO;
import com.google.firebase.FirebaseException;

/**
 * Service to create, read, update and delete patients
 */
public interface PatientService {

    /**
     * Check if user with specified uid exists
     *
     * @param uid user id of the patient
     * @return true if patient entry exists, false otherwise
     */
    Boolean existsByUid(String uid);

    /**
     * Find patient with specified uid
     *
     * @param uid user id of the patient
     * @return patient or null if no patient is found
     */
    PatientDTO findPatientByUid(String uid);

    /**
     * Find patient with specified id
     *
     * @param id id of the patient
     * @return patient or null if no patient is found
     */
    PatientDTO findPatientById(Long id);

    /**
     * Create new patient or update existing one if id is present
     *
     * @param patient the patient to create or update
     * @return patient or null if no patient is found
     */
    PatientDTO createOrUpdatePatient(PatientDTO patient);

    /**
     * Retrieve currently authenticated patient
     *
     * @param uid the uid of the currently authenticated user
     * @return the patient dto of the currently authenticated user
     */
    PatientDTO retrieveCurrentPatient(String uid) throws FirebaseException;

    /**
     * Delete patient by uid
     *
     * @param uid user id of the patient
     */
    void deletePatientByUid(String uid);

}
