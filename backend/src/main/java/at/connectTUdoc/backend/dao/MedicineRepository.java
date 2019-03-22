package at.connectTUdoc.backend.dao;

import at.connectTUdoc.backend.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This class represents the queries for the service layer to interact with the database.
 */
public interface MedicineRepository extends JpaRepository<Medicine, String> {
    /**
     * @param s - a text of the searched medicine
     * @return if found an prepared text of the medicine
     */
    @Override
    Optional<Medicine> findById(String s);
}
