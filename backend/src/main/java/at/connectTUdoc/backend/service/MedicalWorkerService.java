package at.connectTUdoc.backend.service;

import at.connectTUdoc.backend.dto.MedicalWorkerDTO;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.List;

/**
 * This class represents all methods for operating with an MedicalWorker.
 */
public interface MedicalWorkerService {

    /**
     * @param id
     * @return
     * @throws ResourceNotFoundException - if no entry was found
     */
    MedicalWorkerDTO findMedicalWorkerById(long id) throws ResourceNotFoundException;

    /**
     * @param medicalWorker - the MedicalWorker instance to store at the database
     * @return the MedicalWorker instance with the id of the table
     */
    MedicalWorkerDTO createMedicalWorker(MedicalWorkerDTO medicalWorker);

    /**
     * Deletes the medical worker with the id from the database
     *
     * @param id - the id of the medical worker
     */
    void deleteMedicalWorker(Long id);

    /**
     * @param user - the medical worker user to update
     * @return the updated MedicalWorker instance
     */
    MedicalWorkerDTO updateMedicalWorker(MedicalWorkerDTO user);

    /**
     *
     * @return
     * @throws ResourceNotFoundException - if no entry was found
     */
    List<MedicalWorkerDTO> findAllMedicalWorkers() throws ResourceNotFoundException;


    MedicalWorkerDTO findMedicalWorkerByUID(String uid);
}
