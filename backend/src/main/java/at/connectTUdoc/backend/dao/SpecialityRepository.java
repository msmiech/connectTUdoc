package at.connectTUdoc.backend.dao;

import at.connectTUdoc.backend.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class represents the queries for the service layer to interact with the database.
 */
@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long> {

    /**
     * @param name - the name of the speciality
     * @return a list of specialities containing the name ignore case sibling
     */
    List<Speciality> findSpecialitiesBySpecialityNameIgnoreCaseContaining(String name);
}
