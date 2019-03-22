package at.connectTUdoc.backend.repository;

import at.connectTUdoc.backend.dao.OfficeRepository;
import at.connectTUdoc.backend.dao.PatientRepository;
import at.connectTUdoc.backend.model.Office;
import at.connectTUdoc.backend.model.Patient;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
public class PatientRepositoryTest {

    private static final String TEST_PATIENT1_UID = "987654321";
    private static final String TEST_PATIENT2_UID = "123456789";
    private static final String OTHER_UID = "111111111";

    private Patient testPatient1;
    private Patient testPatient2;

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private OfficeRepository officeRepository;


    @Before
    public void setUP() {
        testPatient1 = TestUtilsMedConnect.getPatient1();
        testPatient2 = TestUtilsMedConnect.getPatient2();
        patientRepository.save(testPatient1);
        patientRepository.save(testPatient2);
    }


    @Test
    public void findPatientByUid_shouldReturnPatientWithUid() {
        Patient result = patientRepository.findPatientByUid(TEST_PATIENT2_UID);

        assertEquals(testPatient2.getUid(), result.getUid());
        assertEquals(testPatient2.getSvnr(), result.getSvnr());
        assertEquals(testPatient2.getFirstName(), result.getFirstName());
        assertEquals(testPatient2.getLastName(), result.getLastName());
        assertEquals(testPatient2.geteMail(), result.geteMail());
    }

    @Test
    public void findPatientByUid_shouldReturnNullIfNoPatientFound() {
        Patient result = patientRepository.findPatientByUid(OTHER_UID);

        assertNull(result);
    }

    @Test
    public void existsByUid_shouldReturnTrueIfEntryExists() {
        assertTrue(patientRepository.existsByUid(TEST_PATIENT1_UID));
    }

    @Test
    public void existsByUid_shouldReturnFalseIfEntryDoesNotExist() {
        assertFalse(patientRepository.existsByUid(OTHER_UID));
    }

    @Test
    public void deletePatientByUid_shouldDeletePatientWithUid() {
        assertTrue(patientRepository.existsByUid(TEST_PATIENT1_UID));

        patientRepository.deletePatientByUid(TEST_PATIENT1_UID);

        assertFalse(patientRepository.existsByUid(TEST_PATIENT1_UID));
    }

    @Test
    public void deletePatientByUid_shouldNotDeletePatientIfNoPatientWithUidExists() {
        assertEquals(5, patientRepository.findAll().size());

        patientRepository.deletePatientByUid(OTHER_UID);

        assertEquals(5, patientRepository.findAll().size());
    }

    @Test
    public void getUnconfirmedPatients_shouldReturn2Patients() {
        Office office = officeRepository.getOne(4L);

        List<Patient> patients = patientRepository.getUnconfirmedPatients(office);

        assertEquals(patients.size(),2);
        Assert.assertEquals(patients.get(0),patientRepository.getOne(2L));
        Assert.assertEquals(patients.get(1),patientRepository.getOne(3L));
    }

    @Test
    public void getUnconfirmedPatients_shouldReturn1Patients() {
        Office office = officeRepository.getOne(3L);

        List<Patient> patients = patientRepository.getUnconfirmedPatients(office);

        assertEquals(patients.size(),1);
        Assert.assertEquals(patients.get(0),patientRepository.getOne(3L));
    }

    @Test
    public void getUnconfirmedPatients_shouldReturn0Patients() {
        Office office = officeRepository.getOne(1L);

        List<Patient> patients = patientRepository.getUnconfirmedPatients(office);

        assertEquals(patients.size(),0);
    }




}
