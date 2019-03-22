package at.connectTUdoc.backend.service.impl;

import at.connectTUdoc.backend.dao.ChatMessageRepository;
import at.connectTUdoc.backend.dao.ChatThreadRepository;
import at.connectTUdoc.backend.dto.*;
import at.docTUconnectr.backend.dto.*;
import at.medconnect.backend.dto.*;
import at.ws18_ase_qse_03.backend.dto.*;
import at.connectTUdoc.backend.model.ChatMessage;
import at.connectTUdoc.backend.model.ChatThread;
import at.connectTUdoc.backend.model.Office;
import at.connectTUdoc.backend.model.Patient;
import at.connectTUdoc.backend.service.ChatService;
import at.connectTUdoc.backend.service.FileStorageService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class ChatServiceImpl implements ChatService {

    private static final Logger LOG = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    ChatThreadRepository chatThreadRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    FileStorageService fileStorageServiceImpl;

    @Override
    public ChatThreadDTO createThread(PatientDTO patient, OfficeDTO office) {
        Patient p = modelMapper.map(patient, Patient.class);
        Office o = modelMapper.map(office, Office.class);
        return modelMapper.map(chatThreadRepository.save(new ChatThread(p, o)), ChatThreadDTO.class);
    }

    //@Transactional // required for LOB, which is used in chat attachments
    @Override
    public ChatMessageDTO createMessage(String message, Long threadId, Boolean patientMessage, String senderUid, ChatAttachmentDTO chatAttachmentDTO) throws ResponseStatusException, IOException {

        Optional<ChatThread> chatThread = chatThreadRepository.findById(threadId);
        if (!chatThread.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Kein Chat mit der ID " + threadId + " gefunden!", new ResourceNotFoundException());
        }

        String chatAttachment = "";
        boolean chatAttachmentPresent = false;
        if (chatAttachmentDTO != null) {
            chatAttachmentDTO.validate();
            chatAttachmentPresent = true;
            chatAttachment = storeChatAttachment(chatAttachmentDTO.getFileContent(), generateAttachmentPath(chatAttachmentDTO.getFileName(), chatThread.get().getOffice().getId().toString(), threadId.toString()));
        }

        ChatMessage chatMessage = new ChatMessage(patientMessage, senderUid, chatThread.get(), LocalDateTime.now(), message, chatAttachment, chatAttachmentPresent);

        ChatMessage savedOne = chatMessageRepository.save(chatMessage);
        return convertChatMessageToChatMessageDtoWithAttachment(savedOne, false);
    }

    @Override
    public ChatThreadDTO getChatThreadByThreadID(Long threadId) throws ResourceNotFoundException {
        Optional<ChatThread> chatThread = chatThreadRepository.findById(threadId);
        if (!chatThread.isPresent()) {
            throw new ResourceNotFoundException("Kein Chat mit der ID " + threadId + " gefunden!");
        }
        return modelMapper.map(chatThread.get(), ChatThreadDTO.class);
    }


    @Override
    public List<ChatMessageDTO> findAllChatMessagesByThreadID(Long threadId) throws ResourceNotFoundException, IOException {
        Optional<ChatThread> chatThread = chatThreadRepository.findById(threadId);
        if (!chatThread.isPresent()) {
            throw new ResourceNotFoundException("Kein Chat mit der ID " + threadId + " gefunden!");
        }

        var sortedMessages = chatMessageRepository.findAllByChatThreadOrderByCreateDateTimeAsc(chatThread.get());
        return putAttachmentsToChatMessages(sortedMessages);
    }

    private List<ChatMessageDTO> putAttachmentsToChatMessages(List<ChatMessage> chatMessageList) throws IOException {
        List<ChatMessageDTO> resultList = new ArrayList<>();

        for (ChatMessage content : chatMessageList) {
            if (!StringUtils.isEmpty(content.getChatAttachment())) {
                resultList.add(convertChatMessageToChatMessageDtoWithAttachment(content, false));
            } else {
                resultList.add(modelMapper.map(content, ChatMessageDTO.class));
            }
        }
        return resultList;
    }

    private ChatMessageDTO convertChatMessageToChatMessageDtoWithAttachment(ChatMessage chatMessage, boolean withAttachment) throws IOException {
        ChatMessageDTO input = modelMapper.map(chatMessage, ChatMessageDTO.class);
        if (!ObjectUtils.isEmpty(chatMessage.getChatAttachment())) {
            Resource att = getChatAttachment(chatMessage.getChatAttachment());
            ChatAttachmentDTO chatAttachmentDTO = new ChatAttachmentDTO();
            if (withAttachment) {
                chatAttachmentDTO.setFileContent(Files.readAllBytes(att.getFile().toPath()));
            }
            chatAttachmentDTO.setFileName(att.getFilename());
            var filename = att.getFilename();
            if (filename != null && filename.lastIndexOf('.') != -1) {
                chatAttachmentDTO.setFileType(filename.substring(filename.indexOf('.') + 1));
            }
            input.setChatAttachment(chatAttachmentDTO);
        }
        return input;
    }

    @Override
    public List<ChatThreadDTO> findAllChatThreads(PatientDTO patient) {
        return Arrays.asList(modelMapper.map(chatThreadRepository.findAllByPatient(modelMapper.map(patient, Patient.class)), ChatThreadDTO[].class));
    }

    @Override
    public List<ChatThreadDTO> findAllChatThreads(OfficeDTO office) {
        return Arrays.asList(modelMapper.map(chatThreadRepository.findAllByOffice(modelMapper.map(office, Office.class)), ChatThreadDTO[].class));
    }

    @Override
    public ChatThreadDTO findChatThread(PatientDTO patient, OfficeDTO office) {
        return modelMapper.map(chatThreadRepository.findOneByPatientAndOffice(modelMapper.map(patient, Patient.class), modelMapper.map(office, Office.class)), ChatThreadDTO.class);
    }

    @Override
    public ChatMessageDTO findChatMessageById(Long messageId) throws ResponseStatusException, IOException {
        Optional<ChatMessage> chatMessage = chatMessageRepository.findById(messageId);
        if (!chatMessage.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Kein Chat mit der ID " + messageId + " gefunden!", new ResourceNotFoundException());
        }

        return convertChatMessageToChatMessageDtoWithAttachment(chatMessage.get(), true);
    }

    private Resource getChatAttachment(String fullAttachmentPath) {
        return fileStorageServiceImpl.loadFileAsResource(fullAttachmentPath);
    }

    private String generateAttachmentPath(String fileName, String officeId, String patientId) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();
        return Paths.get(officeId, patientId, dateFormat.format(date) + "_" + fileName).toString();
    }

    private String storeChatAttachment(byte[] attachment, String fullAttachmentPath) {
        return fileStorageServiceImpl.storeFile(new MockMultipartFile("file",
                fullAttachmentPath, "text/plain", attachment));
    }

    public void increaseUnreadMessagesByChatThreadID(Long threadId) {
        Optional<ChatThread> chatThread = chatThreadRepository.findById(threadId);
        if (!chatThread.isPresent()) {
            throw new ResourceNotFoundException("Kein Chat mit der ID " + threadId + " gefunden!");
        }
        ChatThread myChatThread = chatThread.get();
        myChatThread.setUnreadMessages(myChatThread.getUnreadMessages() + 1);
        chatThreadRepository.save(myChatThread);
    }

    public void setZeroUnreadMessagesByChatThreadID(Long threadId) {
        Optional<ChatThread> chatThread = chatThreadRepository.findById(threadId);
        if (!chatThread.isPresent()) {
            throw new ResourceNotFoundException("Kein Chat mit der ID " + threadId + " gefunden!");
        }
        ChatThread myChatThread = chatThread.get();
        myChatThread.setUnreadMessages(0);
        chatThreadRepository.save(myChatThread);
    }

    /*@Override
    public List<ChatAttachmentDTO> findAllChatAttachmentsByThreadID(Long threadId) {
        Optional<ChatThread> chatThread = chatThreadRepository.findById(threadId);
        if (!chatThread.isPresent()) {
            throw new ResourceNotFoundException("No chat thread with id " + threadId + " found!");
        }

        List<ChatAttachment> sortedAttachments = chatAttachmentRepository.findAllByChatThreadOrderByCreateDateTimeAsc(chatThread.get());

        return Arrays.asList(modelMapper.map(sortedAttachments, ChatAttachmentDTO[].class));
    }*/
}
