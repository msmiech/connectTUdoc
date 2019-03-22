package at.connectTUdoc.backend.controller;

import at.connectTUdoc.backend.dto.*;
import at.connectTUdoc.backend.service.*;
import at.docTUconnectr.backend.dto.*;
import at.docTUconnectr.backend.service.*;
import at.medconnect.backend.dto.*;
import at.medconnect.backend.service.*;
import at.ws18_ase_qse_03.backend.dto.*;
import at.ws18_ase_qse_03.backend.service.*;
import com.google.firebase.FirebaseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * This class handles the methods to communicate with the backend and the chats
 */
@Api(value = "api", description = "The Chat REST service")
@RestController
@RequestMapping("/chat")
public class ChatController {

    private static final Logger LOG = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    ChatService chatService;
    @Autowired
    MedicalWorkerService medicalWorkerService;
    @Autowired
    PatientService patientService;
    @Autowired
    OfficeService officeService;
    @Autowired
    FirebaseService firebaseService;

    @GetMapping("/thread")
    @ApiOperation(value = "Get a list of all chat threads between the office and patients", response = ChatThreadDTO.class, responseContainer = "List")
    @CrossOrigin
    @ResponseBody
    public List<ChatThreadDTO> findAllChatThreads() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();

        if (patientService.existsByUid(uid)) {
            return chatService.findAllChatThreads(patientService.findPatientByUid(uid));
        } else {
            MedicalWorkerDTO medicalWorker = medicalWorkerService.findMedicalWorkerByUID(uid);
            return chatService.findAllChatThreads(officeService.findOfficeByMedicalWorker(medicalWorker));
        }
    }

    @GetMapping("/thread/{threadID}")
    @ApiOperation(value = "Get a chat thread by its id", response = ChatThreadDTO.class)
    @CrossOrigin
    @ResponseBody
    public ChatThreadDTO getThreadByThreadID(@Valid @PathVariable("threadID") Long threadID) {
        return chatService.getChatThreadByThreadID(threadID);
    }

    @GetMapping("/officeThread/{officeID}")
    @ApiOperation(value = "Get the chat thread of the current user with a specific office", response = ChatThreadDTO.class)
    @CrossOrigin
    @ResponseBody
    public ChatThreadDTO getPatientsThreadByOfficeID(@Valid @PathVariable("officeID") Long officeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();

        PatientDTO patient = patientService.findPatientByUid(uid);
        OfficeDTO office = officeService.findOfficeById(officeId);

        return chatService.findChatThread(patient, office);
    }


    @PostMapping("/thread")
    @ApiOperation(value = "Create new thread", response = ChatThreadDTO.class)
    @CrossOrigin
    @ResponseBody
    public ChatThreadDTO createThread(@Valid @RequestBody Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();

        PatientDTO patient = null;
        OfficeDTO office = null;
        if (patientService.existsByUid(uid)) {
            patient = patientService.findPatientByUid(uid);
            office = officeService.findOfficeById(id);
        } else {
            patient = patientService.findPatientById(id);
            office = officeService.findOfficeByMedicalWorker(medicalWorkerService.findMedicalWorkerByUID(uid));
        }

        return chatService.createThread(patient, office);
    }

    @GetMapping("/message/{threadID}")
    @ApiOperation(value = "Get a list of all chat messages of a specific chat thread", response = ChatMessageDTO.class, responseContainer = "List")
    @CrossOrigin
    @ResponseBody
    public List<ChatMessageDTO> getAllChatMessagesByThreadID(@Valid @PathVariable("threadID") Long threadID) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        if (!patientService.existsByUid(uid)) {
            chatService.setZeroUnreadMessagesByChatThreadID(threadID);
        }
        return chatService.findAllChatMessagesByThreadID(threadID);
    }


    @PostMapping("/message/{threadID}")
    @ApiOperation(value = "Post a simple message without attachment", response = ChatMessageDTO.class)
    @CrossOrigin
    @ResponseBody
    public ChatMessageDTO createSimpleMessage(@Valid @RequestBody String message, @Valid @PathVariable("threadID") Long threadID) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();

        return createMessage(uid, message, threadID, null);
    }

    /**
     * The following method is a multipart upload call, needs to be respected by client
     *
     * @param message
     * @param threadID
     * @param file
     * @return The created chatmessage object
     */
    @PostMapping("/attachment/{threadID}")
    @ApiOperation(value = "Post a a chat message with file attachment", response = ChatMessageDTO.class)
    @CrossOrigin
    @ResponseBody
    public ChatMessageDTO createMessageWithAttachment(@Valid @PathVariable("threadID") Long threadID, @RequestParam("message") String message, @RequestParam("file") MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();

        ChatAttachmentDTO attachment = null;
        if (!ObjectUtils.isEmpty(file)) {
            try {
                byte[] fileContent = file.getBytes();
                attachment = new ChatAttachmentDTO(); // only create file attachment if it actually exists and it's valid
                attachment.setFileContent(fileContent);
                attachment.setFileName(file.getOriginalFilename()); // cause of test cases the property fileName ist always "file"
                attachment.setFileType(file.getContentType());
            } catch (IOException e) {
                LOG.error("Could not read file attachment of message: " + e.getMessage());
            }
        }

        var messageWithAttachment = createMessage(uid, message, threadID, attachment); // var for debug

        return messageWithAttachment;
    }

    private ChatMessageDTO createMessage(String uid, String message, Long threadID, ChatAttachmentDTO chatAttachmentDTO) throws IOException {

        ChatThreadDTO chatThread = chatService.getChatThreadByThreadID(threadID);
        String officeTopic = "office-" + chatThread.getOffice().getId().toString();
        String patientTopic = chatThread.getPatient().getUid();
        chatService.increaseUnreadMessagesByChatThreadID(threadID);

        if (patientService.existsByUid(uid)) {
            PatientDTO patient = chatThread.getPatient();

            if (!patient.getUid().equals(uid)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Der Benutzer ist kein Teilnehmer an diesem Chat!", new ResourceNotFoundException());
            }

            ChatMessageDTO newMessage = chatService.createMessage(message, threadID, true, uid, chatAttachmentDTO);

            try {
                //Send office id to patient without notification title and body to fetch new messages
                firebaseService.sendNotification("", "", chatThread.getOffice().getId().toString(), patientTopic);
                //Send notification to office
                firebaseService.sendNotification(patient.getFirstName() + " " +patient.getLastName(), "Neue Nachricht", chatThread.getId().toString(), officeTopic);
            } catch (FirebaseException e) {
                LOG.error("Can't send notification!");
            }

            return newMessage;
        } else {

            List<MedicalWorkerDTO> medicalWorkerDTOList = chatService.getChatThreadByThreadID(threadID).getOffice().getOfficeWorkers();
            boolean workingAtOffice = false;
            for (MedicalWorkerDTO medicalWorkerDTO : medicalWorkerDTOList) {
                if (medicalWorkerDTO.getUid().equals(uid)) {
                    workingAtOffice = true;
                    break;
                }
            }
            if (!workingAtOffice) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Der Benutzer ist kein Teilnehmer an diesem Chat!", new ResourceNotFoundException());
            }

            ChatMessageDTO newMessage = chatService.createMessage(message, threadID, false, uid, chatAttachmentDTO);

            try {
                //Send chatThread id to office without notification title and body to fetch new messages
                firebaseService.sendNotification("","", chatThread.getId().toString(), officeTopic);
                //Send notification to patient
                firebaseService.sendNotification(chatThread.getOffice().getName(), "Neue Nachricht", chatThread.getOffice().getId().toString(), patientTopic);
            } catch (FirebaseException e) {
                LOG.error("Can't send notification!");
            }

            return newMessage;
        }
    }

    @GetMapping("/attachment/{messageid}")
    @ApiOperation(value = "Get the attachment of a specific message", response = byte[].class)
    @CrossOrigin
    @ResponseBody
    public ResponseEntity<byte[]> getAttachmentToMessage(@PathVariable long messageid) throws ResponseStatusException, IOException {
        ChatMessageDTO chatMessageDTO = chatService.findChatMessageById(messageid);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + chatMessageDTO.getChatAttachment().getFileName() + "\"")
                .body(chatMessageDTO.getChatAttachment().getFileContent());
    }
}
