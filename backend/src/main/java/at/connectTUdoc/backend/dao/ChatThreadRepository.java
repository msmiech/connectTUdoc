package at.connectTUdoc.backend.dao;

import at.connectTUdoc.backend.model.ChatThread;
import at.connectTUdoc.backend.model.Office;
import at.connectTUdoc.backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class represents the queries for the service layer to interact with the database.
 */
@Repository
public interface ChatThreadRepository extends JpaRepository<ChatThread, Long> {

    List<ChatThread> findAllByOffice(Office office);

    List<ChatThread> findAllByPatient(Patient patient);

    ChatThread findOneByPatientAndOffice(Patient patient, Office office);
}
