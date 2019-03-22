package at.connectTUdoc.backend.service;

import at.connectTUdoc.backend.dao.ChatMessageRepository;
import at.connectTUdoc.backend.dao.ChatThreadRepository;
import at.connectTUdoc.backend.dto.ChatThreadDTO;
import at.connectTUdoc.backend.model.ChatThread;
import at.connectTUdoc.backend.service.impl.ChatServiceImpl;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Before;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import static junit.framework.TestCase.assertEquals;

/**
 * Service Tests for CRUD operations are no benefit, cause there is no ongoing manipulation threw the service-layer.
 * For example some methods are shown to show how to mock these methods.
 */
public class ChatServiceTest {
    @TestConfiguration
    static class ChatServiceTestContextConfiguration {

        @Bean
        public ChatService chatService() {
            return new ChatServiceImpl();
        }

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }

    }

    @Autowired
    ChatService chatService;

    @Autowired
    ModelMapper modelMapper;


    @MockBean
    ChatMessageRepository chatMessageRepository;
    @MockBean
    ChatThreadRepository chatThreadRepository;


    private ChatThreadDTO chatThreadDTO = new ChatThreadDTO();
    private ChatThread chatThread = new ChatThread();

    @Before
    public void setup() {
        chatThread.setPatient(TestUtilsMedConnect.getPatient1());
        chatThread.setOffice(TestUtilsMedConnect.getSingleOffice());
        chatThread.setId(1L);

        chatThreadDTO.setPatient(TestUtilsMedConnect.getPatient1DTO());
        chatThreadDTO.setOffice(TestUtilsMedConnect.getSingleOfficeDTO());
        chatThreadDTO.setId(1L);
    }

    /*@Test
    public void createMessageWithAttachment_ShouldWork() {

        Mockito.when(chatThreadRepository.findById(chatThread.getId())).thenReturn(Optional.of(chatThread));

        ChatThreadDTO chatThreadDTO = chatService.getChatThreadByThreadID(chatThread.getId());
        //var thread = chatThreadRepository.findAll().get(0);
        var testBytes = "AAABB000321".getBytes();

        var attachment = new ChatAttachmentDTO();
        attachment.setFileName("VirtualFile.txt");
        attachment.setFileContent(testBytes);
        attachment.setFileType("test/test");

        var msg = chatService.createMessage("Wir haben kein Problem", chatThreadDTO.getId(), true, attachment);

        var retrievedMessage = chatMessageRepository.findById(msg.getId());

        assertEquals(msg, retrievedMessage);
    }

    @Test(expected = IllegalStateException.class)
    public void createMessageWithAttachment_ShouldFail() {
        var thread = chatThreadRepository.findAll().get(0);
        var testBytes = "AAABB000321".getBytes();

        var attachment = new ChatAttachmentDTO();
        attachment.setFileName("VirtualFile.txt");
        attachment.setFileContent(testBytes);
        attachment.setFileType("test/test");

        var msg = chatService.createMessage("Wir haben ein Problem", thread.getId(), false, attachment);

        var retrievedMessage = chatMessageRepository.findById(msg.getId());

        assertEquals(msg, retrievedMessage);
    }*/

}
