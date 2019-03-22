package at.connectTUdoc.backend.service;

import at.connectTUdoc.backend.dao.PatientRepository;
import at.connectTUdoc.backend.dto.PatientDTO;
import at.connectTUdoc.backend.model.Patient;
import at.connectTUdoc.backend.service.impl.PatientServiceImpl;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Service Tests for CRUD operations are no benefit, cause there is no ongoing manipulation threw the service-layer.
 * For example some methods are shown to show how to mock these methods.
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
public class PatientServiceTest {

    private static final String TEST_PATIENT_UID = "987654321";
    private static final String OTHER_UID = "111111111";

    private static PatientDTO testPatient1;
    private static PatientDTO testPatient2;

    @TestConfiguration
    static class PatientServiceTestContextConfiguration {

        @Bean
        public PatientService patientService() {
            return new PatientServiceImpl();
        }

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }

    }

    @Autowired
    PatientService patientService;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    private FirebaseService firebaseService;

    @BeforeClass
    public static void setUP() {
        testPatient1 = TestUtilsMedConnect.getPatient1DTO();
        testPatient2 = TestUtilsMedConnect.getPatient2DTO();
    }

    @Test
    public void findPatientByUidTest_shouldReturnPatientWithUid() {
        Mockito.when(patientRepository.findPatientByUid(TEST_PATIENT_UID)).thenReturn(modelMapper.map(testPatient1, Patient.class));

        PatientDTO result = patientService.findPatientByUid(TEST_PATIENT_UID);

        assertEquals(testPatient1.getUid(), result.getUid());
        assertEquals(testPatient1.getSvnr(), result.getSvnr());
        assertEquals(testPatient1.getFirstName(), result.getFirstName());
        assertEquals(testPatient1.getLastName(), result.getLastName());
        assertEquals(testPatient1.geteMail(), result.geteMail());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findPatientByUid_shoulThrowResourceNotFoundExceptionIfNoPatientFound() {
        Mockito.when(patientRepository.findPatientByUid(OTHER_UID)).thenReturn(null);

        patientService.findPatientByUid(OTHER_UID);
    }

    @Test
    public void existsByUid_shouldReturnTrueIfEntryExists() {
        Mockito.when(patientRepository.existsByUid(TEST_PATIENT_UID)).thenReturn(true);

        assertTrue(patientService.existsByUid(TEST_PATIENT_UID));
    }

    @Test
    public void existsByUid_shouldReturnFalseIfEntryDoesNotExist() {
        Mockito.when(patientRepository.existsByUid(TEST_PATIENT_UID)).thenReturn(false);

        assertFalse(patientService.existsByUid(OTHER_UID));
    }

    @Test
    public void createOrUpdatePatient_shouldReturnNewPatient() {
        Mockito.when(patientRepository.save(modelMapper.map(testPatient2, Patient.class))).thenReturn(modelMapper.map(testPatient2, Patient.class));

        PatientDTO result = patientService.createOrUpdatePatient(testPatient2);

        assertEquals(testPatient2.getUid(), result.getUid());
        assertEquals(testPatient2.getSvnr(), result.getSvnr());
        assertEquals(testPatient2.getFirstName(), result.getFirstName());
        assertEquals(testPatient2.getLastName(), result.getLastName());
        assertEquals(testPatient2.geteMail(), result.geteMail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createOrUpdatePatient_shouldThrowIllegealArgumentExceptionIfPatientIsNull() {
        patientService.createOrUpdatePatient(null);
    }

    //Currently this function tests only if no exception is thrown
    //Better way to test???
    @Test
    public void deletePatientByUid_shouldDeletePatientWithUid() {
        Mockito.doNothing().when(patientRepository).deletePatientByUid(TEST_PATIENT_UID);

        patientService.deletePatientByUid(TEST_PATIENT_UID);
    }


}
