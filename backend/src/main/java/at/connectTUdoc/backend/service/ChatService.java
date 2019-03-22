package at.connectTUdoc.backend.service;

import at.connectTUdoc.backend.dto.*;
import at.docTUconnectr.backend.dto.*;
import at.medconnect.backend.dto.*;
import at.ws18_ase_qse_03.backend.dto.*;

import java.io.IOException;
import java.util.List;

/**
 * Service interface specification for all chat related operations, i.e. creating new chat threads, creating messages
 * and retrieving them
 */
public interface ChatService {

    /**
     * Creates an ChatThread to collect an store the communication between the Office and the patient
     * @param patient - the patient for the communication
     * @param office - the office for the communication
     * @return
     */
    ChatThreadDTO createThread(PatientDTO patient, OfficeDTO office);

    /**
     * Creates a message as a single item of an needed ChatThread
     * @param message - the message to store
     * @param threadId - the chatthread
     * @param patientMessage - distinguish between office and patient message true/false
     * @param senderUid - the id of the original sender for firebase
     * @param chatAttachment - the attachment of the message
     * @return a message object for future use
     * @throws IOException - if any error occur
     */
    ChatMessageDTO createMessage(String message, Long threadId, Boolean patientMessage, String senderUid, ChatAttachmentDTO chatAttachment) throws IOException;

    /**
     * @param threadID - the id of the thread
     * @return a ChatThread object
     */
    ChatThreadDTO getChatThreadByThreadID(Long threadID);

    /**
     * @param threadId - the id of the thread
     * @return a list of messages regarding to the Thread
     * @throws IOException - if any error occur
     */
    List<ChatMessageDTO> findAllChatMessagesByThreadID(Long threadId) throws IOException;

    /**
     * @param patient - the patient object
     * @return a list of chatThreads, where the patient ist participant
     */
    List<ChatThreadDTO> findAllChatThreads(PatientDTO patient);

    /**
     * @param office - the office object
     * @return a list of chatThreads, where the office is participant
     */
    List<ChatThreadDTO> findAllChatThreads(OfficeDTO office);

    /**
     * @param patient - the patient object
     * @param office - the office object
     * @return a ChatThread, where the patient and the office is participant
     */
    ChatThreadDTO findChatThread(PatientDTO patient, OfficeDTO office);

    /**
     * @param messageId - the id of the message
     * @return a chatmessage object regarding to the id
     * @throws IOException - if any error occur
     */
    ChatMessageDTO findChatMessageById(Long messageId) throws IOException;

    /**
     * Increase the number of unread messages with one
     * @param threadId - the id of the thread
     */
    void increaseUnreadMessagesByChatThreadID(Long threadId);

    /**
     * Set the number of unread messages to zero
     * @param threadId - the id of the thread
     */
    void setZeroUnreadMessagesByChatThreadID(Long threadId);

}
