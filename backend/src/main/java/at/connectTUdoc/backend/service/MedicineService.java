package at.connectTUdoc.backend.service;

import at.connectTUdoc.backend.dto.MedicineDTO;

/**
 * This class represents the services to use for communication with the database.
 */
public interface MedicineService {

    /**
     * @param id id of the medicine to search for
     * @return medicine with the given id
     */
    MedicineDTO getMedicineById(String id);

}
