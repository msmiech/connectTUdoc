package at.connectTUdoc.backend.dao;

import at.connectTUdoc.backend.model.ChatMessage;
import at.connectTUdoc.backend.model.ChatThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * This class represents the queries for the service layer to interact with the database.
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * @param chatThread - the chatThread id
     * @return a list of ChatMessages regarding to the ChatThread
     */
    @Transactional
    List<ChatMessage> findAllByChatThreadOrderByCreateDateTimeAsc(ChatThread chatThread);

}
