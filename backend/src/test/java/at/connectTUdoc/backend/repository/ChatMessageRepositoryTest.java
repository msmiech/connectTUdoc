package at.connectTUdoc.backend.repository;


import at.connectTUdoc.backend.dao.ChatMessageRepository;
import at.connectTUdoc.backend.dao.ChatThreadRepository;
import at.connectTUdoc.backend.dao.OfficeRepository;
import at.connectTUdoc.backend.dao.PatientRepository;
import at.connectTUdoc.backend.model.ChatMessage;
import at.connectTUdoc.backend.model.ChatThread;
import at.connectTUdoc.backend.model.Office;
import at.connectTUdoc.backend.model.Patient;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import at.ws18_ase_qse_03.backend.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * This class contains the test cases for the repository tests for the specific class and the database transactions
 */
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest(showSql = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChatMessageRepositoryTest {
    private ChatMessage chatMessage1;
    private ChatMessage chatMessage2;
    private ChatMessage chatMessage3;

    private ChatThread chatThread1;
    private ChatThread chatThread2;
    private ChatThread chatThread3;

    private Office office1;
    private Office office2;
    private Patient patient1;
    private Patient patient2;

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatThreadRepository chatThreadRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private OfficeRepository officeRepository;

    // get File from resource
    @Value(value = "classpath:Test.png")
    private Resource testImage;

    @Before
    public void setUP() {

        chatMessage1 = TestUtilsMedConnect.getMessageEarlier();
        chatMessage2 = TestUtilsMedConnect.getMessageLater();
        chatMessage3 = TestUtilsMedConnect.getOtherMessage();

        chatThread1 = chatMessage1.getChatThread();
        chatThread2 = chatMessage3.getChatThread();
        chatThread3 = TestUtilsMedConnect.getChatThread1();
        chatThread3.setId(42L);

        office1 = chatThread1.getOffice();
        office2 = chatThread2.getOffice();
        patient1 = chatThread1.getPatient();
        patient2 = chatThread2.getPatient();

        patientRepository.save(patient1);
        patientRepository.save(patient2);
        officeRepository.save(office1);
        officeRepository.save(office2);
        chatThreadRepository.save(chatThread1);
        chatThreadRepository.save(chatThread2);

        chatMessageRepository.save(chatMessage2);
        chatMessageRepository.save(chatMessage3);
        chatMessageRepository.save(chatMessage1);
    }

    @Test
    public void findAllByChatThreadOrderByCreateDateTimeAsc_shouldReturnChatMessageFromSize5() {
        List<ChatMessage> result = chatMessageRepository.findAllByChatThreadOrderByCreateDateTimeAsc(chatThread1);

        assertEquals(5, result.size());
    }

    @Test
    public void findAllByChatThreadOrderByCreateDateTimeAsc_shouldReturnChatMessages4() {
        List<ChatMessage> result = chatMessageRepository.findAllByChatThreadOrderByCreateDateTimeAsc(chatThread2);

        assertEquals(4, result.size());
    }

    @Test
    public void findAllByChatThreadOrderByCreateDateTimeAsc_shouldReturnEmptyList() {
        List<ChatMessage> result = chatMessageRepository.findAllByChatThreadOrderByCreateDateTimeAsc(chatThread3);

        assertEquals(0, result.size());
    }

    /*@Test
    public void loadAMessageWithAttachment_shouldWorkWithoutError() throws IOException {
        ChatMessage messageAtt = TestUtilsMedConnect.getMessageEarlier();
        messageAtt.setId(4L);
        ChatAttachment chatAttachment = new ChatAttachment();
        chatAttachment.setId(4L);

        // get File from resource
        File file = testImage.getFile();

        chatAttachment.setFileName(file.getName());
        chatAttachment.setFileType(FileType.PNG.toString());
        //byte[] uploadFile = Files.readAllBytes(file.toPath());
        chatAttachment.setFileContent(uploadFile);
        messageAtt.setChatAttachment(chatAttachment);
        chatMessageRepository.save(messageAtt);
        Optional<ChatMessage> optResponse = chatMessageRepository.findById(messageAtt.getId());

        assertTrue(optResponse.isPresent());
        assertNotNull(optResponse.get().getChatAttachment());
        assertEquals(optResponse.get().getChatAttachment().getFileName(), file.getName());
        // Only this version of compare with equals works, cause assertEquals compares on identity and not value
        assertTrue(Arrays.equals(uploadFile, optResponse.get().getChatAttachment().getFileContent()));
    }*/
}
