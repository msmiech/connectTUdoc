package at.connectTUdoc.backend;

import at.connectTUdoc.backend.controller.*;
import at.connectTUdoc.backend.dto.*;
import at.connectTUdoc.backend.repository.*;
import at.connectTUdoc.backend.service.*;
import at.docTUconnectr.backend.controller.*;
import at.docTUconnectr.backend.dto.*;
import at.docTUconnectr.backend.repository.*;
import at.docTUconnectr.backend.service.*;
import at.medconnect.backend.controller.*;
import at.medconnect.backend.dto.*;
import at.medconnect.backend.repository.*;
import at.medconnect.backend.service.*;
import at.ws18_ase_qse_03.backend.controller.*;
import at.ws18_ase_qse_03.backend.dto.*;
import at.ws18_ase_qse_03.backend.repository.*;
import at.ws18_ase_qse_03.backend.service.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static junit.framework.TestCase.assertTrue;

/**
 * This class contains the test suite for all tests of the backend
 */
@Ignore
@RunWith(Suite.class)
@Suite.SuiteClasses({
        MedicineRepositoryTest.class,
        OfficeRepositoryTest.class,
        MedicalWorkerRepositoryTest.class,
        PatientRepositoryTest.class,
        AppointmentRepositoryTest.class,

        MedicineServiceTest.class,
        AppointmentServiceTest.class,
        ChatMessageRepositoryTest.class,
        ChatThreadRepositoryTest.class,

        OfficeServiceTest.class,
        FirebaseServiceTest.class,
        PatientServiceTest.class,

        MedicineDTOTest.class,
        AddressDTOTest.class,
        AppointmentDTOTest.class,
        ChatMessageDTOTest.class,
        ChatThreadDTOTest.class,
        MedicalWorkerDTOTest.class,
        OfficeDTOTest.class,
        OfficeHourDTOTest.class,
        SpecialityDTOTest.class,

        MedicineControllerTest.class,
        OfficeControllerTest.class,
        PatientControllerTest.class,
        ChatControllerTest.class,
        AppointmentControllerTest.class
})
public class BackendApplicationTests {

    @Test
    public void contextLoads() {
        assertTrue(true);
    }

}
