package at.connectTUdoc.backend.controller;

import at.connectTUdoc.backend.controller.abstracts.AbstractWebTest;
import at.connectTUdoc.backend.dto.ChatAttachmentDTO;
import at.connectTUdoc.backend.exception.UserNotAuthenticatedException;
import at.connectTUdoc.backend.service.FirebaseService;
import at.connectTUdoc.backend.dto.ChatMessageDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;

import static org.junit.Assert.*;

/**
 * This class contains the office controller web testing cases
 */
public class ChatControllerTest extends AbstractWebTest {

    private static final String URI_CREATE_MESSAGE_WITH_ATTACHMENT = "/chat/attachment/";
    private static final String URI_GET_MESSAGE_WITH_ATTACHMENT = "/chat/attachment/";
    private static final String URI_CREATE_SIMPLE_MESSAGE = "/chat/message/";
    private static final String URI_GET_ALL_CHATTHREADS = "/chat/thread";
    private static final String URI_GET_CHATTHREAD_BY_ID = "/chat/thread/";
    private static final String URI_GET_CHATTHREAD_BY_OFFICE_ID = "/chat/officeThread/";
    private static final String URI_GET_ALL_MESSAGES_BY_THREAD_ID = "/chat/message/";
    private static final String PATH_ATTACHMENT_MESSAGE_PARAM_NAME = "message";
    private static final String UID_FOR_PATIENT_WITH_ID_2 = "wCjBKGC7EMZo49VTMCDb92toPXf2";
    private static final String UID_FOR_PATIENT_WITH_ID_3 = "9VB82zVsNLaOiKDOuRfS6VmLzwx2";
    private static final String UID_FOR_MEDICALWORKER_WITH_ID_1 = "bS9nqS956xUFYYy26bWmFvT8noo1";

    @MockBean
    Authentication authentication;
    @MockBean
    SecurityContext securityContext;
    @MockBean
    FirebaseService firebaseService;

    // get File from resource
    @Value(value = "classpath:Test.png")
    private Resource testImage;

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getAllChatMessagesByThreadId_shouldReturn6Messages() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_2);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String uri = URI_GET_ALL_MESSAGES_BY_THREAD_ID + 1;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[{\"id\":1,\"patientMessage\":true,\"senderUid\":null,\"chatThread\":{\"id\":1,\"patient\":{\"id\":1,\"uid\":\"1qxo6HiFksd7UYXXY9gtcaGKYCu2\",\"preTitle\":\"Dr.\",\"firstName\":\"Hans\",\"lastName\":\"Mueller\",\"posTitle\":\"\",\"eMail\":\"hans@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"1111111111\"},\"office\":{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},\"unreadMessages\":0},\"createDateTime\":\"2017-02-11 08:11\",\"message\":\"Mir geht es nicht gut!\",\"chatAttachment\":null,\"chatAttachmentPresent\":false},{\"id\":2,\"patientMessage\":false,\"senderUid\":null,\"chatThread\":{\"id\":1,\"patient\":{\"id\":1,\"uid\":\"1qxo6HiFksd7UYXXY9gtcaGKYCu2\",\"preTitle\":\"Dr.\",\"firstName\":\"Hans\",\"lastName\":\"Mueller\",\"posTitle\":\"\",\"eMail\":\"hans@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"1111111111\"},\"office\":{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},\"unreadMessages\":0},\"createDateTime\":\"2017-02-11 11:01\",\"message\":\"Was fehlt Ihnen denn\",\"chatAttachment\":null,\"chatAttachmentPresent\":false},{\"id\":3,\"patientMessage\":true,\"senderUid\":null,\"chatThread\":{\"id\":1,\"patient\":{\"id\":1,\"uid\":\"1qxo6HiFksd7UYXXY9gtcaGKYCu2\",\"preTitle\":\"Dr.\",\"firstName\":\"Hans\",\"lastName\":\"Mueller\",\"posTitle\":\"\",\"eMail\":\"hans@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"1111111111\"},\"office\":{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},\"unreadMessages\":0},\"createDateTime\":\"2017-02-11 12:12\",\"message\":\"Bauchschmerzen\",\"chatAttachment\":null,\"chatAttachmentPresent\":false},{\"id\":4,\"patientMessage\":false,\"senderUid\":null,\"chatThread\":{\"id\":1,\"patient\":{\"id\":1,\"uid\":\"1qxo6HiFksd7UYXXY9gtcaGKYCu2\",\"preTitle\":\"Dr.\",\"firstName\":\"Hans\",\"lastName\":\"Mueller\",\"posTitle\":\"\",\"eMail\":\"hans@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"1111111111\"},\"office\":{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},\"unreadMessages\":0},\"createDateTime\":\"2017-02-11 13:07\",\"message\":\"Ok, ich werde schauen was ich tun kann.\",\"chatAttachment\":null,\"chatAttachmentPresent\":false},{\"id\":5,\"patientMessage\":true,\"senderUid\":null,\"chatThread\":{\"id\":1,\"patient\":{\"id\":1,\"uid\":\"1qxo6HiFksd7UYXXY9gtcaGKYCu2\",\"preTitle\":\"Dr.\",\"firstName\":\"Hans\",\"lastName\":\"Mueller\",\"posTitle\":\"\",\"eMail\":\"hans@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"1111111111\"},\"office\":{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},\"unreadMessages\":0},\"createDateTime\":\"2017-02-11 13:34\",\"message\":\"Vielen Dank!\",\"chatAttachment\":null,\"chatAttachmentPresent\":false},{\"id\":6,\"patientMessage\":false,\"senderUid\":null,\"chatThread\":{\"id\":1,\"patient\":{\"id\":1,\"uid\":\"1qxo6HiFksd7UYXXY9gtcaGKYCu2\",\"preTitle\":\"Dr.\",\"firstName\":\"Hans\",\"lastName\":\"Mueller\",\"posTitle\":\"\",\"eMail\":\"hans@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"1111111111\"},\"office\":{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},\"unreadMessages\":0},\"createDateTime\":\"2017-02-11 13:50\",\"message\":\"Nehmen sie einmal taeglich XYZ\",\"chatAttachment\":null,\"chatAttachmentPresent\":false}]"
                , content);
    }

    @Test
    public void getAllChatMessagesByThreadId_shouldThrowResourceNotFoundException() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_2);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String uri = URI_GET_ALL_MESSAGES_BY_THREAD_ID + 5874;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(ResourceNotFoundException.class, mvcResult.getResolvedException().getClass());
    }

    @Test
    public void getChatThreadByOfficeID_shouldReturnChatThreadWithId2() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_2);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String uri = URI_GET_CHATTHREAD_BY_OFFICE_ID + 1;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"id\":2,\"patient\":{\"id\":2,\"uid\":\"wCjBKGC7EMZo49VTMCDb92toPXf2\",\"preTitle\":\"\",\"firstName\":\"Franz\",\"lastName\":\"Walder\",\"posTitle\":\"Bsc.\",\"eMail\":\"franz@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"2222111111\"},\"office\":{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},\"unreadMessages\":0}"
                , content);
    }

    @Test
    public void getChatThreadByOfficeID_shouldThrowResourceNotFoundExceptionWithCode404() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_2);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String uri = URI_GET_CHATTHREAD_BY_OFFICE_ID + 3456;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(ResourceNotFoundException.class, mvcResult.getResolvedException().getClass());
    }

    @Test
    public void getChatThreadByOfficeID_shouldThrowResourceNotFoundExceptionWrongPatient() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_MEDICALWORKER_WITH_ID_1);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String uri = URI_GET_CHATTHREAD_BY_OFFICE_ID + 1;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(ResourceNotFoundException.class, mvcResult.getResolvedException().getClass());
        assertEquals("Kein Patient mit der ID: bS9nqS956xUFYYy26bWmFvT8noo1 gespeichert.",
                mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void getChatThreadById_shouldReturnChatThreadWithId1() throws Exception {
        String uri = URI_GET_CHATTHREAD_BY_ID + 1;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"id\":1,\"patient\":{\"id\":1,\"uid\":\"1qxo6HiFksd7UYXXY9gtcaGKYCu2\",\"preTitle\":\"Dr.\",\"firstName\":\"Hans\",\"lastName\":\"Mueller\",\"posTitle\":\"\",\"eMail\":\"hans@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"1111111111\"},\"office\":{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},\"unreadMessages\":0}"
                , content);
    }

    @Test
    public void getChatThreadById_shouldThrowResourceNotFoundExceptionWithCode404() throws Exception {
        String uri = URI_GET_CHATTHREAD_BY_ID + 45895;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(ResourceNotFoundException.class, mvcResult.getResolvedException().getClass());
    }

    @Test
    public void findAllChatThreads_shouldReturnTwoChatThreads() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_2);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_GET_ALL_CHATTHREADS)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[{\"id\":2,\"patient\":{\"id\":2,\"uid\":\"wCjBKGC7EMZo49VTMCDb92toPXf2\",\"preTitle\":\"\",\"firstName\":\"Franz\",\"lastName\":\"Walder\",\"posTitle\":\"Bsc.\",\"eMail\":\"franz@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"2222111111\"},\"office\":{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},\"unreadMessages\":0},{\"id\":16,\"patient\":{\"id\":2,\"uid\":\"wCjBKGC7EMZo49VTMCDb92toPXf2\",\"preTitle\":\"\",\"firstName\":\"Franz\",\"lastName\":\"Walder\",\"posTitle\":\"Bsc.\",\"eMail\":\"franz@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"2222111111\"},\"office\":{\"id\":3,\"name\":\"Praxis Dr. Hubert\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":3,\"street\":\"Herzgasse\",\"number\":\"10\",\"door\":\"6\",\"floor\":\"6\",\"place\":\"Vienna\",\"zip\":1100,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"psycho@farma.fa\",\"officehours\":[{\"id\":11,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"MONTAG\"},{\"id\":12,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"DIENSTAG\"},{\"id\":13,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"MITTWOCH\"},{\"id\":14,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":15,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"FREITAG\"}],\"officeWorkers\":[{\"id\":5,\"uid\":\"Ea3D4hYZjAU8UNMM3JYpwACMI303\",\"preTitle\":\"\",\"firstName\":\"Manuel\",\"lastName\":\"Meister\",\"posTitle\":\"Dr.\",\"eMail\":\"manuel.meister@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":5,\"specialityName\":\"Technician\"}],\"type\":\"DOCTOR\"}]},\"unreadMessages\":0}]",
                content);
    }

    @Test
    public void findAllChatThreads_shouldThrowUserNotAuthenticatedException() throws Exception {
        Mockito.when(authentication.getName()).thenReturn("asdkljdfg0afglk");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_GET_ALL_CHATTHREADS)).andReturn();

        assertEquals(401, mvcResult.getResponse().getStatus());
        assertEquals(UserNotAuthenticatedException.class, mvcResult.getResolvedException().getClass());
        assertEquals("Kein Benutzer verzeichnet mit der uid asdkljdfg0afglk !",
                mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void createSimpleMessage_shouldCreateANewSimpleMessage() throws Exception {
        String compareText = "Hugo wird es bringen";

        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_2);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String uri = URI_CREATE_SIMPLE_MESSAGE + 2;

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON).content(compareText)).andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        ChatMessageDTO chatMessageDTO = mapFromJson(content, ChatMessageDTO.class);
        chatMessageDTO.setCreateDateTime(null); // cause static comparison over a time is not possible

        assertEquals("{\"id\":142,\"patientMessage\":true,\"senderUid\":\"wCjBKGC7EMZo49VTMCDb92toPXf2\",\"chatThread\":{\"id\":2,\"patient\":{\"id\":2,\"uid\":\"wCjBKGC7EMZo49VTMCDb92toPXf2\",\"preTitle\":\"\",\"firstName\":\"Franz\",\"lastName\":\"Walder\",\"posTitle\":\"Bsc.\",\"eMail\":\"franz@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"2222111111\"},\"office\":{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},\"unreadMessages\":1},\"createDateTime\":null,\"message\":\"Hugo wird es bringen\",\"chatAttachment\":null,\"chatAttachmentPresent\":false}",
                mapToJson(chatMessageDTO));
    }

    @Test
    public void createSimpleMessage_shouldThrowResponseStatusExceptionWithCode409WrongThreadId() throws Exception {
        String compareText = "Hugo wird es bringen";

        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_2);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String uri = URI_CREATE_SIMPLE_MESSAGE + 1;

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON).content(compareText)).andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(409, mvcResult.getResponse().getStatus());
        assertEquals(ResponseStatusException.class, mvcResult.getResolvedException().getClass());

    }

    @Test
    public void createSimpleMessage_shouldThrowResourceNotFoundExceptionWithCode404() throws Exception {
        String compareText = "Hugo wird es bringen";

        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_2);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String uri = URI_CREATE_SIMPLE_MESSAGE + 1985;

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON).content(compareText)).andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(ResourceNotFoundException.class, mvcResult.getResolvedException().getClass());

    }

    @Test
    public void createSimpleMessage_shouldThrowResponseStatusExceptionWithCode409WrongUser() throws Exception {
        String compareText = "Hugo wird es bringen";

        Mockito.when(authentication.getName()).thenReturn("kajdfkasldoef");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String uri = URI_CREATE_SIMPLE_MESSAGE + 1;

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON).content(compareText)).andReturn();

        assertEquals(409, mvcResult.getResponse().getStatus());
        assertEquals(ResponseStatusException.class, mvcResult.getResolvedException().getClass());
        assertEquals("409 CONFLICT \"Der Benutzer ist kein Teilnehmer an diesem Chat!\"; nested exception is org.springframework.data.rest.webmvc.ResourceNotFoundException: Resource not found!",
                mvcResult.getResolvedException().getMessage());

    }

    @Test
    public void createChatMessageWithAttachment_shouldWorkWithoutError() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_2);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String compareText = "Message mit Upload";
        String fileName = "Test.png";

        // get File from resource
        FileInputStream input = new FileInputStream(testImage.getFile());

        MockMultipartFile file = new MockMultipartFile(
                "file", // has to be named like requested from the controller!!!
                fileName,
                "image/png",
                input);

        String uri = URI_CREATE_MESSAGE_WITH_ATTACHMENT + 2;

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.
                multipart(uri)
                .file(file)
                .param(PATH_ATTACHMENT_MESSAGE_PARAM_NAME, compareText)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        byte[] compareContent = new FileInputStream(testImage.getFile()).readAllBytes();

        ChatMessageDTO chatMessageDTO = mapFromJson(content, ChatMessageDTO.class);
        chatMessageDTO.setCreateDateTime(null); // cause static comparison over a time is not possible
        ChatAttachmentDTO attachmentDTO = chatMessageDTO.getChatAttachment();
        chatMessageDTO.setChatAttachment(null);

        assertEquals("{\"id\":142,\"patientMessage\":true,\"senderUid\":\"wCjBKGC7EMZo49VTMCDb92toPXf2\",\"chatThread\":{\"id\":2,\"patient\":{\"id\":2,\"uid\":\"wCjBKGC7EMZo49VTMCDb92toPXf2\",\"preTitle\":\"\",\"firstName\":\"Franz\",\"lastName\":\"Walder\",\"posTitle\":\"Bsc.\",\"eMail\":\"franz@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"2222111111\"},\"office\":{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},\"unreadMessages\":1},\"createDateTime\":null,\"message\":\"Message mit Upload\",\"chatAttachment\":null,\"chatAttachmentPresent\":true}",
            mapToJson(chatMessageDTO));
        assertNull(attachmentDTO.getFileContent());
    }

    @Test
    public void createChatMessageWithAttachment_shouldThrowResponseStatusExceptionWithCode409() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_2);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String compareText = "Message mit Upload";
        String fileName = "Test.png";

        FileInputStream input = new FileInputStream(testImage.getFile());
        MockMultipartFile file = new MockMultipartFile(
                "file", // has to be named like requested from the controller!!!
                fileName,
                "image/png",
                input);

        String uri = URI_CREATE_MESSAGE_WITH_ATTACHMENT + 1;

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.
                multipart(uri)
                .file(file)
                .param(PATH_ATTACHMENT_MESSAGE_PARAM_NAME, compareText)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(409, mvcResult.getResponse().getStatus());
        assertEquals(ResponseStatusException.class, mvcResult.getResolvedException().getClass());
    }

    @Test
    public void createChatMessageWithAttachment_shouldThrowResourceNotFoundExceptionWithCode404() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_2);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String compareText = "Message mit Upload";
        String fileName = "Test.png";

        // get File from resource
        FileInputStream input = new FileInputStream(testImage.getFile());

        MockMultipartFile file = new MockMultipartFile(
                "file", // has to be named like requested from the controller!!!
                fileName,
                "image/png",
                input);

        String uri = URI_CREATE_MESSAGE_WITH_ATTACHMENT + 2698;

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.
                multipart(uri)
                .file(file)
                .param(PATH_ATTACHMENT_MESSAGE_PARAM_NAME, compareText)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(ResourceNotFoundException.class, mvcResult.getResolvedException().getClass());
    }

    @Test
    public void getChatMessageWithAttachment_shouldWorkWithoutError() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_2);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String compareText = "Message mit Upload";
        String fileName = "Test.png";

        // get File from resource
        FileInputStream input = new FileInputStream(testImage.getFile());

        MockMultipartFile file = new MockMultipartFile(
                "file", // has to be named like requested from the controller!!!
                fileName,
                "image/png",
                input);

        String uri = URI_CREATE_MESSAGE_WITH_ATTACHMENT + 2;

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.
                multipart(uri)
                .file(file)
                .param(PATH_ATTACHMENT_MESSAGE_PARAM_NAME, compareText)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        ChatMessageDTO chatMessageDTO = super.mapFromJson(content, ChatMessageDTO.class);

        assertEquals(chatMessageDTO.getMessage(), compareText);
        assertNotNull(chatMessageDTO.getChatAttachment());
        assertTrue(chatMessageDTO.getChatAttachment().getFileName().contains(fileName));

        // load the attachment from the id
        uri = URI_GET_MESSAGE_WITH_ATTACHMENT + chatMessageDTO.getId();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
        ).andReturn();

        status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
        byte[] contentByte = mvcResult.getResponse().getContentAsByteArray();
        // original file content
        byte[] compareContent = new FileInputStream(testImage.getFile()).readAllBytes();

        assertArrayEquals(compareContent
                , contentByte);

    }

    @Test
    public void getChatMessageWithAttachment_shouldThrowResponseStatusExceptionWithCode409() throws Exception {
        // load the attachment from the id
        String uri = URI_GET_MESSAGE_WITH_ATTACHMENT + 9999;

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
        ).andReturn();

        assertEquals(409, mvcResult.getResponse().getStatus());
        assertEquals(ResponseStatusException.class, mvcResult.getResolvedException().getClass());
    }
}
