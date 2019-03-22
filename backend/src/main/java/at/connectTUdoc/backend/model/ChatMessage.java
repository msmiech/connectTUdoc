package at.connectTUdoc.backend.model;

import at.connectTUdoc.backend.helper.MedConnectConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This class represents the sub-model of the database
 */
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Boolean patientMessage;
    private String senderUid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatThreadId")
    private ChatThread chatThread;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = MedConnectConstants.JSON_FORMAT_LOCALDATETIME)
    private LocalDateTime createDateTime;
    @Column(columnDefinition="TEXT")
    private String message;
    private Boolean chatAttachmentPresent;
    private String chatAttachment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(patientMessage, that.patientMessage) &&
                Objects.equals(senderUid, that.senderUid) &&
                Objects.equals(chatThread, that.chatThread) &&
                Objects.equals(createDateTime, that.createDateTime) &&
                Objects.equals(message, that.message) &&
                Objects.equals(chatAttachmentPresent, that.chatAttachmentPresent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientMessage, senderUid, chatThread, createDateTime, message, chatAttachment, chatAttachmentPresent);
    }

    public ChatMessage() {

    }

    public ChatMessage(Boolean patientMessage, String senderUid, ChatThread chatThread, LocalDateTime createDateTime, String message, String chatAttachment, Boolean chatAttachmentPresent) {
        this.patientMessage = patientMessage;
        this.senderUid = senderUid;
        this.chatThread = chatThread;
        this.createDateTime = createDateTime;
        this.message = message;
        this.chatAttachment = chatAttachment;
        this.chatAttachmentPresent = chatAttachmentPresent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPatientMessage()
    {
        return patientMessage;
    }

    public void setPatientMessage(Boolean patientMessage)
    {
        this.patientMessage = patientMessage;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public ChatThread getChatThread() {
        return chatThread;
    }

    public void setChatThread(ChatThread chatThread) {
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

    public String getChatAttachment() {
        return chatAttachment;
    }

    public void setChatAttachment(String chatAttachment) {
        this.chatAttachment = chatAttachment;
    }

    public Boolean getChatAttachmentPresent() {
        return chatAttachmentPresent;
    }

    public void setChatAttachmentPresent(Boolean chatAttachmentPresent) {
        this.chatAttachmentPresent = chatAttachmentPresent;
    }
}
