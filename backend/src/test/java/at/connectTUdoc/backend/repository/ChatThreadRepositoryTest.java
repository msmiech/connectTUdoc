package at.connectTUdoc.backend.repository;


import at.connectTUdoc.backend.dao.ChatThreadRepository;
import at.connectTUdoc.backend.dao.OfficeRepository;
import at.connectTUdoc.backend.dao.PatientRepository;
import at.connectTUdoc.backend.model.ChatThread;
import at.connectTUdoc.backend.model.Office;
import at.connectTUdoc.backend.model.Patient;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * This class contains the test cases for the repository tests for the specific class and the database transactions
 */
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest(showSql = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChatThreadRepositoryTest
{
    private ChatThread chatThread1;
    private ChatThread chatThread2;

    private Office office;
    private Office otherOffice;
    private Patient patient1;
    private Patient patient2;
    private Patient otherPatient;

    @Autowired
    private ChatThreadRepository chatThreadRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private OfficeRepository officeRepository;

    @Before
    public void setUP() {

        chatThread1 = TestUtilsMedConnect.getChatThread1();
        chatThread2 = TestUtilsMedConnect.getChatThread2();

        office = chatThread1.getOffice();
        otherOffice = TestUtilsMedConnect.getSingleOffice();
        otherOffice.setId(42L);
        patient1 = chatThread1.getPatient();
        patient2 = chatThread2.getPatient();
        otherPatient = TestUtilsMedConnect.getPatient1();
        otherPatient.setId(101010L);

        patientRepository.save(patient1);
        patientRepository.save(patient2);
        officeRepository.save(office);
        officeRepository.save(otherOffice);
        chatThreadRepository.save(chatThread1);
        chatThreadRepository.save(chatThread2);
    }

    @Test
    public void findAllByOffice_shouldReturnBothChatThreads()
    {
        List<ChatThread> result =  chatThreadRepository.findAllByOffice(office);

        assertEquals(2, result.size());
        assertEquals(result.get(0).getId(), chatThread1.getId());
        assertEquals(result.get(1).getId(), chatThread2.getId());
        assertEquals(result.get(0).getOffice().getId(), office.getId());
        assertEquals(result.get(1).getOffice().getId(), office.getId());
        assertEquals(result.get(0).getPatient().getId(), patient1.getId());
        assertEquals(result.get(1).getPatient().getId(), patient2.getId());
    }

    @Test
    public void findAllByOffice_shouldReturnEmptyList()
    {
        List<ChatThread> result =  chatThreadRepository.findAllByOffice(otherOffice);

        assertEquals(0, result.size());
    }

    @Test
    public void findAllByPatient_shouldReturnChatThread1()
    {
        List<ChatThread> result = chatThreadRepository.findAllByPatient(patient1);


        assertEquals(1, result.size());
        assertEquals(result.get(0).getId(), chatThread1.getId());
        assertEquals(result.get(0).getOffice().getId(), office.getId());
        assertEquals(result.get(0).getPatient().getId(), patient1.getId());
    }

    @Test
    public void findAllByPatient_shouldReturnChatThread2()
    {
        List<ChatThread> result = chatThreadRepository.findAllByPatient(patient2);

        assertEquals(1, result.size());
        assertEquals(result.get(0).getId(), chatThread2.getId());
        assertEquals(result.get(0).getOffice().getId(), office.getId());
        assertEquals(result.get(0).getPatient().getId(), patient2.getId());
    }


    @Test
    public void findAllByPatient_shouldReturnEmptyList()
    {
        List<ChatThread> result = chatThreadRepository.findAllByPatient(otherPatient);

        assertEquals(0, result.size());
    }


    @Test
    public void findOneByPatientAndOffice_shouldReturnChatThread1()
    {
        ChatThread result = chatThreadRepository.findOneByPatientAndOffice(patient1, office);

        assertEquals(result.getId(), chatThread1.getId());
        assertEquals(result.getOffice().getId(), office.getId());
        assertEquals(result.getPatient().getId(), patient1.getId());
    }

    @Test
    public void findOneByPatientAndOffice_shouldReturnChatThread2()
    {
        ChatThread result = chatThreadRepository.findOneByPatientAndOffice(patient2, office);

        assertEquals(result.getId(), chatThread2.getId());
        assertEquals(result.getOffice().getId(), office.getId());
        assertEquals(result.getPatient().getId(), patient2.getId());
    }

    @Test
    public void findOneByPatientAndOffice_shouldReturnNullWrongOffice()
    {
        ChatThread result = chatThreadRepository.findOneByPatientAndOffice(patient1, otherOffice);

        assertNull(result);
    }

    @Test
    public void findOneByPatientAndOffice_shouldReturnNullWrongPatient()
    {
        ChatThread result = chatThreadRepository.findOneByPatientAndOffice(otherPatient, office);

        assertNull(result);
    }
}
