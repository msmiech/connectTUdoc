package at.connectTUdoc.backend.service;

import at.connectTUdoc.backend.dao.*;
import at.connectTUdoc.backend.dto.MedicalWorkerDTO;
import at.connectTUdoc.backend.dto.OfficeDTO;
import at.connectTUdoc.backend.dto.PatientDTO;
import at.connectTUdoc.backend.dto.RegistrationCodeDTO;
import at.connectTUdoc.backend.model.ChatThread;
import at.connectTUdoc.backend.model.MedicalWorker;
import at.connectTUdoc.backend.model.Office;
import at.connectTUdoc.backend.model.Patient;
import at.connectTUdoc.backend.service.impl.MedicalWorkerServiceImpl;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import at.docTUconnectr.backend.dao.*;
import at.connectTUdoc.backend.service.impl.OfficeServiceImpl;
import at.medconnect.backend.dao.*;
import at.ws18_ase_qse_03.backend.dao.*;
import org.junit.Assert;
import org.junit.Before;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * Service Tests for CRUD operations are no benefit, cause there is no ongoing manipulation threw the service-layer.
 * For example some methods are shown to show how to mock these methods.
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
public class OfficeServiceTest {

    private static final String TEST_OFFICE_NAME = "Hugo";
    private static ConcurrentHashMap<Long, RegistrationCodeDTO> registration = new ConcurrentHashMap<>();

    @TestConfiguration
    static class OfficeServiceTestContextConfiguration {

        @Bean
        public OfficeService officeService() {
            return new OfficeServiceImpl(registration);
        }

        @Bean
        public MedicalWorkerService medicalWorkerService() {
            return new MedicalWorkerServiceImpl();
        }

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }

    }

    @Autowired
    OfficeService officeService;

    @Autowired
    MedicalWorkerService medicalWorkerService;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    private SpecialityRepository specialityRepository;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private MedicalWorkerRepository medicalWorkerRepository;

    @MockBean
    private OfficeRepository officeRepository;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    ChatThreadRepository chatThreadRepository;


    private MedicalWorkerDTO medicalWorkerDTO;
    private MedicalWorkerDTO medNotFoundDTO;
    private OfficeDTO officeDTO;
    private List<OfficeDTO> officeDTOS;
    private List<Office> offices;
    private PatientDTO patientDTO;


    @Before
    public void setUP() {

        medicalWorkerDTO = TestUtilsMedConnect.getSingleMedicalWorkerDTO();
        medNotFoundDTO = new MedicalWorkerDTO();
        officeDTO = TestUtilsMedConnect.getSingleOfficeDTO();
        patientDTO = TestUtilsMedConnect.getPatient1DTO();


        Mockito.when(medicalWorkerRepository.save(modelMapper.map(medicalWorkerDTO, MedicalWorker.class)))
                .thenReturn(modelMapper.map(medicalWorkerDTO, MedicalWorker.class));

        Mockito.when(medicalWorkerRepository.findAll())
                .thenReturn(TestUtilsMedConnect.getMedicalWorkers(1));


        Mockito.when(officeRepository.save(modelMapper.map(officeDTO, Office.class)))
                .thenReturn(modelMapper.map(officeDTO, Office.class));


        officeDTOS = Arrays.asList(modelMapper.map(TestUtilsMedConnect.getOffices(1), OfficeDTO[].class));
        officeDTOS.get(0).setName(TEST_OFFICE_NAME);
        offices = Arrays.asList(modelMapper.map(officeDTOS, Office[].class));
        Mockito.when(officeRepository.findAll())
                .thenReturn(offices);

        Mockito.when(officeRepository.findOfficesByName(TEST_OFFICE_NAME))
                .thenReturn(offices);

        Mockito.when(officeRepository.findOfficesByName(""))
                .thenThrow(new ResourceNotFoundException());


        Mockito.when(officeRepository.findOfficeByOfficeWorkersIn(modelMapper.map(medicalWorkerDTO, MedicalWorker.class)))
                .thenReturn(Optional.of(TestUtilsMedConnect.getSingleOffice()));

        Mockito.when(officeRepository.findOfficeByOfficeWorkersIn(modelMapper.map(medNotFoundDTO, MedicalWorker.class)))
                .thenReturn(Optional.empty());
    }

    @Test
    public void createMedicalWorker_InsertNewMedicalWorker() {
        MedicalWorkerDTO med = medicalWorkerService.createMedicalWorker(medicalWorkerDTO);

        assertEquals(med, medicalWorkerDTO);
    }

    @Test
    public void createOffice_InsertNewOffice() {
        OfficeDTO resOfficeDTO = officeService.createOffice(officeDTO);

        Assert.assertEquals(resOfficeDTO, TestUtilsMedConnect.getSingleOfficeDTO());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createOffice_shouldRaiseIllegalArgumentException() {
        officeService.createOffice(null);

        fail();
    }

    @Test
    public void findAllOffices_FindOneOffice() {
        List<OfficeDTO> foundOffices = officeService.findAllOffices();

        assertEquals(foundOffices, officeDTOS);
    }

    @Test
    public void findOfficeByName_FindOfficeWithNameHugo() {
        List<OfficeDTO> officesByName = officeService.findOfficesByName(TEST_OFFICE_NAME);

        assertNotNull(officesByName);
        assertEquals(officesByName, officeDTOS);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findOfficeByName_shouldThrowResourceNotFoundException() {
        officeService.findOfficesByName("");

        fail();
    }

    @Test
    public void findOfficeByMedicalWorker_FindOfficeWithTestMedicalWorker() {
        OfficeDTO res = officeService.findOfficeByMedicalWorker(TestUtilsMedConnect.getSingleMedicalWorkerDTO());

        Assert.assertEquals(res, TestUtilsMedConnect.getSingleOfficeDTO());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findOfficeByMedicalWorker_shouldThrowResourceNotFoundException() {
        OfficeDTO res = officeService.findOfficeByMedicalWorker(new MedicalWorkerDTO());

        fail();
    }

    @Test
    public void getOfficesByPatient_shouldReturnAllOfficesOfPatient() {
        Mockito.when(officeRepository.findOfficesByOfficePatientsIn(TestUtilsMedConnect.getPatient1())).thenReturn(offices);

        List<OfficeDTO> foundOffices = officeService.getOfficesByPatient(patientDTO);

        assertEquals(foundOffices, officeDTOS);
    }

    @Test
    public void getOfficesByPatient_shouldReturnEmptyListIfPatientHasNoOffices() {
        Mockito.when(officeRepository.findOfficesByOfficePatientsIn(any())).thenReturn(new ArrayList<>());

        List<OfficeDTO> foundOffices = officeService.getOfficesByPatient(patientDTO);

        assertEquals(0, foundOffices.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOfficesByPatient_shouldThrowIllegalArgumentExceptionIfPatientIsNull() {
        officeService.getOfficesByPatient(null);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getUnconfirmedPatients_shouldThrowResourceNotFoundException() {
        Mockito.when(officeRepository.findById(17L)).thenReturn(Optional.empty());
        officeService.getUnconfirmedPatients(17L);
    }


   @Test
    public void getUnconfirmedPatients_shouldReturn1Patients() {
       Mockito.when(officeRepository.findById(5L)).thenReturn(Optional.of(TestUtilsMedConnect.getSingleOffice()));
       Mockito.when(patientRepository.getUnconfirmedPatients(TestUtilsMedConnect.getSingleOffice())).thenReturn(List.of(TestUtilsMedConnect.getPatient1()));
       List<PatientDTO> patients = officeService.getUnconfirmedPatients(5L);

       assertEquals(patients.size(),1);
       Assert.assertEquals(patients.get(0),TestUtilsMedConnect.getPatient1DTO());
    }

    @Test
    public void getUnconfirmedPatients_shouldReturn2Patients() {
        Mockito.when(officeRepository.findById(5L)).thenReturn(Optional.of(TestUtilsMedConnect.getSingleOffice()));
        Mockito.when(patientRepository.getUnconfirmedPatients(TestUtilsMedConnect.getSingleOffice())).thenReturn(List.of(TestUtilsMedConnect.getPatient1(),TestUtilsMedConnect.getPatient2()));
        List<PatientDTO> patients = officeService.getUnconfirmedPatients(5L);

        assertEquals(patients.size(),2);
        Assert.assertEquals(patients.get(0),TestUtilsMedConnect.getPatient1DTO());
        Assert.assertEquals(patients.get(1),TestUtilsMedConnect.getPatient2DTO());
    }

    @Test
    public void getUnconfirmedPatients_shouldReturn0Patients() {
        Mockito.when(officeRepository.findById(5L)).thenReturn(Optional.of(TestUtilsMedConnect.getSingleOffice()));
        Mockito.when(patientRepository.getUnconfirmedPatients(TestUtilsMedConnect.getSingleOffice())).thenReturn(new ArrayList<Patient>());
        List<PatientDTO> patients = officeService.getUnconfirmedPatients(5L);

        assertEquals(patients.size(),0);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void addPatientToOffice_shouldThrowResourceNotFoundExceptionForOffice() {
        RegistrationCodeDTO codeDTO = new RegistrationCodeDTO();
        Mockito.when(officeRepository.findById(17L)).thenReturn(Optional.empty());
        Mockito.when(patientRepository.findById(17L)).thenReturn(Optional.of(TestUtilsMedConnect.getPatient1()));
        registration.put(17L,codeDTO);
        officeService.addPatientToOffice(17L,17L,new RegistrationCodeDTO());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void addPatientToOffice_shouldThrowResourceNotFoundExceptionForPatient() {
        RegistrationCodeDTO codeDTO = new RegistrationCodeDTO();
        Mockito.when(officeRepository.findById(5L)).thenReturn(Optional.of(TestUtilsMedConnect.getSingleOffice()));
        Mockito.when(patientRepository.findById(17L)).thenReturn(Optional.empty());
        registration.put(17L,codeDTO);
        officeService.addPatientToOffice(5L,17L, new RegistrationCodeDTO());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void addPatientToOffice_shouldThrowResourceNotFoundExceptionDueToMissingRegistrationCode() {
        RegistrationCodeDTO codeDTO = new RegistrationCodeDTO();
        Office office = TestUtilsMedConnect.getSingleOffice();
        Mockito.when(officeRepository.findById(5L)).thenReturn(Optional.of(TestUtilsMedConnect.getSingleOffice()));
        Mockito.when(patientRepository.findById(17L)).thenReturn(Optional.of(TestUtilsMedConnect.getPatient1()));
        ChatThread chatThread = new ChatThread(TestUtilsMedConnect.getPatient1(), TestUtilsMedConnect.getSingleOffice());
        Mockito.when(chatThreadRepository.save(chatThread)).thenReturn(chatThread);
        office.addOfficePatient(TestUtilsMedConnect.getPatient1());

        officeService.addPatientToOffice(5L,17L,codeDTO);
    }

    @Test(expected = ResponseStatusException.class)
    public void addPatientToOffice_shouldThrowResponseStatusExceptionDueToExpiredCode() {
        RegistrationCodeDTO codeDTO = new RegistrationCodeDTO();
        RegistrationCodeDTO codeDTO2 = new RegistrationCodeDTO();
        codeDTO.setExpire(LocalDateTime.now().minusMinutes(5));
        codeDTO2.setCode(codeDTO.getCode());
        Office office = TestUtilsMedConnect.getSingleOffice();
        Mockito.when(officeRepository.findById(5L)).thenReturn(Optional.of(TestUtilsMedConnect.getSingleOffice()));
        Mockito.when(patientRepository.findById(17L)).thenReturn(Optional.of(TestUtilsMedConnect.getPatient1()));
        ChatThread chatThread = new ChatThread(TestUtilsMedConnect.getPatient1(), TestUtilsMedConnect.getSingleOffice());
        Mockito.when(chatThreadRepository.save(chatThread)).thenReturn(chatThread);
        office.addOfficePatient(TestUtilsMedConnect.getPatient1());
        registration.put(17L,codeDTO);

        officeService.addPatientToOffice(5L,17L,codeDTO2);
        assertNull(registration.get(17L));
    }

    @Test(expected = ResponseStatusException.class)
    public void addPatientToOffice_shouldThrowResponseStatusExceptionDueToWrongRegistrationCode() {
        RegistrationCodeDTO codeDTO = new RegistrationCodeDTO();
        RegistrationCodeDTO codeDTO2 = new RegistrationCodeDTO();
        codeDTO2.setCode(codeDTO.getCode()-1);
        Office office = TestUtilsMedConnect.getSingleOffice();
        Mockito.when(officeRepository.findById(5L)).thenReturn(Optional.of(TestUtilsMedConnect.getSingleOffice()));
        Mockito.when(patientRepository.findById(17L)).thenReturn(Optional.of(TestUtilsMedConnect.getPatient1()));
        ChatThread chatThread = new ChatThread(TestUtilsMedConnect.getPatient1(), TestUtilsMedConnect.getSingleOffice());
        Mockito.when(chatThreadRepository.save(chatThread)).thenReturn(chatThread);
        office.addOfficePatient(TestUtilsMedConnect.getPatient1());
        registration.put(17L,codeDTO);

        officeService.addPatientToOffice(5L,17L,codeDTO2);
    }


    @Test
    public void addPatientToOffice_shouldAddNewPatient() {
        RegistrationCodeDTO codeDTO = new RegistrationCodeDTO();
        Office office = TestUtilsMedConnect.getSingleOffice();
        Mockito.when(officeRepository.findById(5L)).thenReturn(Optional.of(TestUtilsMedConnect.getSingleOffice()));
        Mockito.when(patientRepository.findById(17L)).thenReturn(Optional.of(TestUtilsMedConnect.getPatient1()));
        ChatThread chatThread = new ChatThread(TestUtilsMedConnect.getPatient1(), TestUtilsMedConnect.getSingleOffice());
        Mockito.when(chatThreadRepository.save(chatThread)).thenReturn(chatThread);
        registration.put(17L,codeDTO);
        office.addOfficePatient(TestUtilsMedConnect.getPatient1());

        Mockito.when(officeRepository.save(office)).thenReturn(office);
        officeService.addPatientToOffice(5L,17L,codeDTO);
        assertNull(registration.get(17L));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteAppointmentsByOfficeANDPatient_shouldThrowResourceNotFoundExceptionForOffice() {
        Mockito.when(officeRepository.findById(17L)).thenReturn(Optional.empty());
        Mockito.when(patientRepository.findById(17L)).thenReturn(Optional.empty());
        officeService.deleteAppointmentsByOfficeANDPatient(17L,17L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteAppointmentsByOfficeANDPatient_shouldThrowResourceNotFoundExceptionForPatient() {
        Mockito.when(officeRepository.findById(5L)).thenReturn(Optional.of(TestUtilsMedConnect.getSingleOffice()));
        Mockito.when(patientRepository.findById(17L)).thenReturn(Optional.empty());
        officeService.deleteAppointmentsByOfficeANDPatient(5L,17L);
    }

    @Test
    public void deleteAppointmentsByOfficeANDPatient_shouldDeleteOneAppointment() {
        Mockito.when(officeRepository.findById(5L)).thenReturn(Optional.of(TestUtilsMedConnect.getSingleOffice()));
        Mockito.when(patientRepository.findById(17L)).thenReturn(Optional.of(TestUtilsMedConnect.getPatient1()));
        Mockito.doNothing().when(appointmentRepository).deleteAppointmentByOfficeAndPatient(TestUtilsMedConnect.getSingleOffice(),TestUtilsMedConnect.getPatient1());
        officeService.deleteAppointmentsByOfficeANDPatient(5L,17L);
    }
    @Test(expected = ResourceNotFoundException.class)
    public void getRegistrationCode_shouldThrowResourceNotFoundException() {
        Mockito.when(patientRepository.findById(17L)).thenReturn(Optional.empty());
        officeService.getRegistrationCode(17L);
    }

    @Test
    public void getRegistrationCode_shouldCreateNewRegistrationCode() {
        Mockito.when(patientRepository.findById(17L)).thenReturn(Optional.of(TestUtilsMedConnect.getPatient1()));
        RegistrationCodeDTO codeDTO = officeService.getRegistrationCode(17L);

        assertEquals(registration.get(17L),codeDTO);
    }
}
