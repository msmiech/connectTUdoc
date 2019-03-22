package at.connectTUdoc.backend.repository;

import at.connectTUdoc.backend.dao.MedicalWorkerRepository;
import at.connectTUdoc.backend.dao.OfficeRepository;
import at.connectTUdoc.backend.dao.PatientRepository;
import at.connectTUdoc.backend.model.*;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import at.docTUconnectr.backend.model.*;
import at.medconnect.backend.model.*;
import at.ws18_ase_qse_03.backend.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * This class contains the test cases for the repository tests of office database transactions
 */
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest(showSql = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OfficeRepositoryTest {

    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private MedicalWorkerRepository medicalWorkerRepository;
    @Autowired
    private PatientRepository patientRepository;

    private Office office;

    @Before
    public void setUP() {
        office = TestUtilsMedConnect.getSingleOffice();
    }


    @Test
    public void createOffice_shouldCreateNewOffice() {
        Office createdOffice = officeRepository.save(office);
        assertEquals(office,createdOffice);
    }

    @Test
    public void updateOffice_Correct() {

        Office savedOffice = officeRepository.getOne(1L);

        int officeHoursBefore = savedOffice.getOfficehours().size();

        // edit something
        savedOffice.setPhone("77777");
        savedOffice.setFax("897452");
        savedOffice.getOfficehours().add(TestUtilsMedConnect.getSingleOfficeHour("08:00", "10:00", DayType.FREITAG));
        savedOffice.setEmail("offices@transient.doc");

        // Save
        officeRepository.save(savedOffice);
        // getSpecial one

        Office edited = officeRepository.getOne(savedOffice.getId());


        assertEquals(edited.getEmail(), savedOffice.getEmail());
        assertEquals(edited.getPhone(), savedOffice.getPhone());
        assertEquals(edited.getFax(), savedOffice.getFax());
        assertTrue(edited.getOfficehours().size() > officeHoursBefore);


    }

    @Test
    public void deleteOffice_shouldDeleteExistingOffice() {

        Office foundOffice = officeRepository.findById(1L).orElse(null);
        assertNotNull(foundOffice);
        // delete
        officeRepository.deleteById(1L);

        // find them
        Office findOffice = officeRepository.findById(1L).orElse(null);
        assertNull(findOffice);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteOffice_shouldThrowEmptyResultDataAccessException() {
        // delete not existing office
        officeRepository.deleteById(20L);
    }


    @Test(expected = ResourceNotFoundException.class)
    public void findOffice_ResourceNotFoundException() {
        Long id = 20L;

        // find them
        officeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found"));
    }

    @Test
    public void findOfficeById_shouldFindOfficeWithGivenId() {
        Office foundById = officeRepository.findById(1L).orElse(null);

        assertNotNull(foundById);
    }

    @Test
    public void findOfficeByName_shouldFindOfficeWithGivenName() {
        List<Office> officesByName = officeRepository.findOfficesByName("Praxis Dr. Hubert");

        assertNotNull(officesByName);
        assertEquals(2, officesByName.size());
    }

    @Test
    public void findOfficeByName_shouldNotFindAnyOffices() {
        List<Office> officesByName = officeRepository.findOfficesByName("Dr. Dolittle");

        assertNotNull(officesByName);
        assertEquals(0, officesByName.size());
    }

    @Test
    public void findOfficeByMedicalWorker_FindMedicalWorker1() {
        MedicalWorker med1 = TestUtilsMedConnect.getSingleMedicalWorker();
        med1.setId(9890L);
        med1.setSpecialities(new ArrayList<>()); // has to be unique
        MedicalWorker med2 = TestUtilsMedConnect.getSingleMedicalWorker();
        med2.setId(10980L);
        med2.setSpecialities(new ArrayList<>()); // has to be unique
        MedicalWorker med3 = TestUtilsMedConnect.getSingleMedicalWorker();
        med3.setId(109780L);
        med3.setSpecialities(new ArrayList<>()); // has to be unique
        MedicalWorker med4 = TestUtilsMedConnect.getSingleMedicalWorker();
        med4.setId(0L);
        med4.setSpecialities(new ArrayList<>()); // has to be unique

        Office office1 = TestUtilsMedConnect.getSingleOffice();
        office1.setId(1090L);
        Office office2 = TestUtilsMedConnect.getSingleOffice();
        office2.setId(1049L);

        List<MedicalWorker> medicalWorkers = new ArrayList<>();
        medicalWorkers.add(med1);
        medicalWorkers.add(med2);

        office1.setOfficeWorkers(medicalWorkers);

        medicalWorkers = new ArrayList<>();
        medicalWorkers.add(med3);
        medicalWorkers.add(med4);

        office2.setOfficeWorkers(medicalWorkers);

        Office savedFirst = officeRepository.save(office1);
        Office savedSecond = officeRepository.save(office2);

        // set ID of worker
        med1 = savedFirst.getOfficeWorkers().get(0);

        Optional<Office> opt = officeRepository.findOfficeByOfficeWorkersIn(med1);

        Office medicalWorkerOffice = new Office();

        if(opt.isPresent()){
            medicalWorkerOffice = opt.get();
        }

        assertEquals(savedFirst, medicalWorkerOffice);
        assertNotEquals(savedFirst, savedSecond);
    }

    @Test
    public void findOfficeByMedicalWorker_shouldNotFoundAnOffice() {
        MedicalWorker med1 = TestUtilsMedConnect.getSingleMedicalWorker();
        MedicalWorker med3 = TestUtilsMedConnect.getSingleMedicalWorker();

        Office office1 = TestUtilsMedConnect.getSingleOffice();
        Office office2 = TestUtilsMedConnect.getSingleOffice();

        List<MedicalWorker> medicalWorkers = new ArrayList<>();
        medicalWorkers.add(med1);

        office1.setOfficeWorkers(medicalWorkers);

        medicalWorkers = new ArrayList<>();
        medicalWorkers.add(med3);

        office2.setOfficeWorkers(medicalWorkers);

        MedicalWorker work = TestUtilsMedConnect.getSingleMedicalWorker();
        work.setSpecialities(new ArrayList<>());
        work.setType(MedWorkerType.ASSISTANT);
        work.setId(9097L);

        // Medical worker has to exist in the database!!!!
        MedicalWorker med5 = medicalWorkerRepository.save(work);

        Optional<Office> opt = officeRepository.findOfficeByOfficeWorkersIn(med5);

        assertFalse(opt.isPresent());
    }

    @Test
    public void findOfficesByOfficePatientsIn_shouldFindAllOfficesOfPatient() {
        Patient patient = patientRepository.getOne(2L);

        List<Office> patientOffices = officeRepository.findOfficesByOfficePatientsIn(patient);

        assertNotNull(patientOffices);
        assertEquals(3, patientOffices.size());
    }

    @Test
    public void findOfficesByOfficePatientsIn_shouldReturnEmptyListIfPatientHasNoOffices() {
        Patient patient = patientRepository.getOne(10L);

        List<Office> patientOffices = officeRepository.findOfficesByOfficePatientsIn(patient);

        assertNotNull(patientOffices);
        assertEquals(0, patientOffices.size());
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void findOfficesByOfficePatientsIn_shouldThrowInvalidDataAccessApiUsageExceptionIfPatientIsNull() {
        officeRepository.findOfficesByOfficePatientsIn(null);
    }

}
