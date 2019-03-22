package at.connectTUdoc.backend.dto;

import at.connectTUdoc.backend.helper.MedConnectConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This class represents the layer between the database and the service layer and transforms the model object to an DTO and vise versa.
 */
public class ChatMessageDTO extends AbstractDTO {

    private Long id;
    private boolean patientMessage;
    private String senderUid;
    private ChatThreadDTO chatThread;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = MedConnectConstants.JSON_FORMAT_LOCALDATETIME)
    private LocalDateTime createDateTime;
    private String message;
    private ChatAttachmentDTO chatAttachment;
    private boolean chatAttachmentPresent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessageDTO that = (ChatMessageDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(message, that.message) &&
                Objects.equals(chatThread.getId(), that.chatThread.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, chatThread.getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public boolean isPatientMessage() {
        return patientMessage;
    }

    public void setPatientMessage(boolean patientMessage) {
        this.patientMessage = patientMessage;
    }

    public ChatThreadDTO getChatThread() {
        return chatThread;
    }

    public void setChatThread(ChatThreadDTO chatThread) {
        this.chatThread = chatThread;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatAttachmentDTO getChatAttachment() {
        return chatAttachment;
    }

    public void setChatAttachment(ChatAttachmentDTO attachments) {
        this.chatAttachment = attachments;
    }

    public boolean isChatAttachmentPresent() {
        return chatAttachmentPresent;
    }

    public void setChatAttachmentPresent(boolean chatAttachmentPresent) {
        this.chatAttachmentPresent = chatAttachmentPresent;
    }
}
