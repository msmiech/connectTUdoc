package at.connectTUdoc.backend.dao;

import at.connectTUdoc.backend.model.MedicalWorker;
import at.connectTUdoc.backend.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class represents the queries for the service layer to interact with the database.
 */
@Repository
public interface MedicalWorkerRepository extends JpaRepository<MedicalWorker, Long> {
    // for special queries do it like ...
    /*
    @Query("SELECT con FROM Contact con  WHERE con.phoneType=(:pType) AND con.lastName= (:lName)")
    List<Contact> findByLastNameAndPhoneType(@Param("pType") PhoneType pType, @Param("lName") String lName);
    */

    List<MedicalWorker> findMedicalWorkerByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(String firstName, String lastName);

    List<MedicalWorker> findMedicalWorkerBySpecialities(List<Speciality> specialities);

    List<MedicalWorker> findMedicalWorkersByEMail(String email);

    MedicalWorker findMedicalWorkerByUidEquals(String uid);
}
