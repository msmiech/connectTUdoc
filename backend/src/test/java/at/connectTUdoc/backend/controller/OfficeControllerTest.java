package at.connectTUdoc.backend.controller;

import at.connectTUdoc.backend.controller.abstracts.AbstractWebTest;
import at.connectTUdoc.backend.dto.OfficeDTO;
import at.connectTUdoc.backend.dto.PatientDTO;
import at.connectTUdoc.backend.dto.RegistrationCodeDTO;
import at.connectTUdoc.backend.exception.UserNotAuthenticatedException;
import at.connectTUdoc.backend.service.FirebaseService;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class contains the office controller web testing cases
 */
public class OfficeControllerTest extends AbstractWebTest {

    private static final String URI_FIND_ALL_OFFICES = "/office";
    private static final String URI_FIND_OFFICE_BY_ID = "/office/";
    private static final String URI_CREATE_NEW_OFFICE = "/office";
    private static final String URI_DELETE_OFFICE_BY_ID = "/office/";
    private static final String URI_UPDATE_OFFICE = "/office";
    private static final String URI_GET_PATIENT_OFFICES = "/office/patientOffices";
    private static final String URI_GET_MEDICALWORKER_OFFICES = "/office/searchOfficeByLoggedInMedicalUser";
    private static final String URI_GET_PATIENTS_OF_OFFICE = "/office/officePatients";
    private static final String URI_FIND_OFFICE_BY_SEARCH_TEXT = "/office/searchText/";
    private static final String URI_FIND_OFFICE_BY_SEARCH_TEXT_NAME_ONLY = "/office/searchTextNameOnly/";
    private static final String URI_FIND_OFFICE_BY_SEARCH_TEXT_SPECIALITY_ONLY = "/office/searchTextSpecialityNameOnly/";
    private static final String URI_FIND_OFFICE_BY_SEARCH_TEXT_DOCTOR_NAME_ONLY = "/office/searchTextDoctorNameOnly/";
    private static final String URI_FIND_OFFICE_BY_MEDICAL_WORKER_ID = "/office/searchOfficeByMedicalWorker/";
    private static final String URI_GET_REGISTRATIONCODE = "/office/patient/registrationcode";
    private static final String UID_FOR_PATIENT_WITH_ID_1 = "1qxo6HiFksd7UYXXY9gtcaGKYCu2";
    private static final String UID_FOR_PATIENT_WITH_ID_2 = "wCjBKGC7EMZo49VTMCDb92toPXf2";
    private static final String UID_FOR_PATIENT_WITH_ID_3 = "9VB82zVsNLaOiKDOuRfS6VmLzwx2";
    private static final String UID_FOR_MEDICALWORKER_WITH_ID_1 = "bS9nqS956xUFYYy26bWmFvT8noo1";



    @MockBean
    Authentication authentication;
    @MockBean
    SecurityContext securityContext;
    @MockBean
    FirebaseService firebaseService;

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void findAllOffices_shouldFind4Offices() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_FIND_ALL_OFFICES)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("[{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},{\"id\":2,\"name\":\"Praxis Dr. Hubert\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":2,\"street\":\"Loewengasse\",\"number\":\"5\",\"door\":\"7\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1030,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"dagobert@duck.eh\",\"officehours\":[{\"id\":5,\"beginTime\":\"09:00\",\"endTime\":\"12:00\",\"daytype\":\"DIENSTAG\"},{\"id\":6,\"beginTime\":\"14:00\",\"endTime\":\"17:00\",\"daytype\":\"DIENSTAG\"},{\"id\":7,\"beginTime\":\"09:30\",\"endTime\":\"13:30\",\"daytype\":\"MITTWOCH\"},{\"id\":8,\"beginTime\":\"08:00\",\"endTime\":\"12:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":9,\"beginTime\":\"14:00\",\"endTime\":\"16:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":10,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"FREITAG\"}],\"officeWorkers\":[{\"id\":3,\"uid\":\"PmgPFW3CsZXht2IuOFiAqldozVV2\",\"preTitle\":\"MSc\",\"firstName\":\"Berta\",\"lastName\":\"Hadel\",\"posTitle\":\"Dr\",\"eMail\":\"berta.hadel@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":3,\"specialityName\":\"Dentist\"}],\"type\":\"DOCTOR\"},{\"id\":4,\"uid\":\"igi3pmYNRRWeQurhojiX8fDJmGj2\",\"preTitle\":\"\",\"firstName\":\"Mist\",\"lastName\":\"Eria\",\"posTitle\":\"Dipl.Ing\",\"eMail\":\"mist.eria@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":4,\"specialityName\":\"Common Allergic\"}],\"type\":\"ASSISTANT\"}]},{\"id\":3,\"name\":\"Praxis Dr. Hubert\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":3,\"street\":\"Herzgasse\",\"number\":\"10\",\"door\":\"6\",\"floor\":\"6\",\"place\":\"Vienna\",\"zip\":1100,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"psycho@farma.fa\",\"officehours\":[{\"id\":11,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"MONTAG\"},{\"id\":12,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"DIENSTAG\"},{\"id\":13,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"MITTWOCH\"},{\"id\":14,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":15,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"FREITAG\"}],\"officeWorkers\":[{\"id\":5,\"uid\":\"Ea3D4hYZjAU8UNMM3JYpwACMI303\",\"preTitle\":\"\",\"firstName\":\"Manuel\",\"lastName\":\"Meister\",\"posTitle\":\"Dr.\",\"eMail\":\"manuel.meister@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":5,\"specialityName\":\"Technician\"}],\"type\":\"DOCTOR\"}]},{\"id\":4,\"name\":\"Praxis Dr. Mueller\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":4,\"street\":\"Karlsplatz\",\"number\":\"3\",\"door\":\"1\",\"floor\":\"1\",\"place\":\"Vienna\",\"zip\":1010,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"iron@man.dc\",\"officehours\":[{\"id\":16,\"beginTime\":\"09:00\",\"endTime\":\"13:00\",\"daytype\":\"DIENSTAG\"},{\"id\":17,\"beginTime\":\"09:00\",\"endTime\":\"12:00\",\"daytype\":\"MITTWOCH\"},{\"id\":18,\"beginTime\":\"14:00\",\"endTime\":\"18:00\",\"daytype\":\"MITTWOCH\"}],\"officeWorkers\":[{\"id\":6,\"uid\":\"V3ZYOd3fn5b8q7INqUG06Akh5uI3\",\"preTitle\":\"\",\"firstName\":\"Eva\",\"lastName\":\"Jung\",\"posTitle\":\"Dr.\",\"eMail\":\"eva.jung@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":6,\"specialityName\":\"Psychology\"}],\"type\":\"DOCTOR\"}]}]"
                ,content);
    }

    @Test
    public void createOffice_shouldAddNewOfficeIntoDatabase() throws Exception {
        OfficeDTO insertOffice = TestUtilsMedConnect.getSingleOfficeDTOWithoutAppointments();
        String inputJson = super.mapToJson(insertOffice);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URI_CREATE_NEW_OFFICE)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        OfficeDTO createdOffice = super.mapFromJson(content,OfficeDTO.class);

        assertEquals(insertOffice,createdOffice);

        assertEquals("{\"id\":5,\"name\":\"Arztpraxis\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":5,\"street\":\"Wimbledon\",\"number\":\"12\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1235,\"city\":\"Test 1\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":19,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":20,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":21,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":22,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":7,\"uid\":\"uid7\",\"preTitle\":\"Vamp\",\"firstName\":\"Marcus\",\"lastName\":\"Dracula\",\"posTitle\":\"ire\",\"eMail\":\"test@test.at\",\"privateKey\":\"1234\",\"publicKey\":\"5678\",\"specialities\":[{\"id\":7,\"specialityName\":\"Human\"}],\"type\":\"DOCTOR\"},{\"id\":8,\"uid\":\"uid8\",\"preTitle\":\"Vamp\",\"firstName\":\"Marcus\",\"lastName\":\"Dracula\",\"posTitle\":\"ire\",\"eMail\":\"test@test.at\",\"privateKey\":\"1234\",\"publicKey\":\"5678\",\"specialities\":[{\"id\":8,\"specialityName\":\"Human\"}],\"type\":\"DOCTOR\"},{\"id\":9,\"uid\":\"uid9\",\"preTitle\":\"Vamp\",\"firstName\":\"Marcus\",\"lastName\":\"Dracula\",\"posTitle\":\"ire\",\"eMail\":\"test@test.at\",\"privateKey\":\"1234\",\"publicKey\":\"5678\",\"specialities\":[{\"id\":9,\"specialityName\":\"Human\"}],\"type\":\"DOCTOR\"}]}"
                , content);
    }

    @Test
    public void findOfficeByID_shouldGetOfficeWithId1() throws Exception {
        String uri = URI_FIND_OFFICE_BY_ID + 1;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String  content = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]}"
                ,content);
    }

    @Test
    public void findOfficeByID_shouldThrowResourceNotFoundExceptionWithCode404() throws Exception {
        String uri = URI_FIND_OFFICE_BY_ID + 0;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(ResourceNotFoundException.class,mvcResult.getResolvedException().getClass());
    }

    @Test
    public void deleteOfficeById_DeletesOfficeWithId1() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(URI_DELETE_OFFICE_BY_ID + 1)
                ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void updateOffice_UpdateOfficeAndValidateWithEquals() throws Exception {
        OfficeDTO office = TestUtilsMedConnect.getSingleOfficeDTO();
        String inputJson = super.mapToJson(office);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URI_CREATE_NEW_OFFICE)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        OfficeDTO createdOffice = super.mapFromJson(content,OfficeDTO.class);

        createdOffice.setName("Edited");
        createdOffice.setEmail("edit@edit.ed");
        createdOffice.setFax("666");

        String updateJson = super.mapToJson(createdOffice);
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(URI_UPDATE_OFFICE)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(updateJson)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        content = mvcResult.getResponse().getContentAsString();
        OfficeDTO updateOffice = super.mapFromJson(content,OfficeDTO.class);

        assertEquals(updateOffice,createdOffice);
    }

    @Test
    public void findOfficeBySearchText_shouldFind1OfficesByName() throws Exception{
        String uri = URI_FIND_OFFICE_BY_SEARCH_TEXT + "Praxis Dr. Mayer";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]}]",
                content);
    }

    @Test
    public void findOfficeBySearchText_shouldFindTwoOfficesByDoctorName() throws Exception{
        String uri = URI_FIND_OFFICE_BY_SEARCH_TEXT + "Manuel";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[{\"id\":3,\"name\":\"Praxis Dr. Hubert\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":3,\"street\":\"Herzgasse\",\"number\":\"10\",\"door\":\"6\",\"floor\":\"6\",\"place\":\"Vienna\",\"zip\":1100,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"psycho@farma.fa\",\"officehours\":[{\"id\":11,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"MONTAG\"},{\"id\":12,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"DIENSTAG\"},{\"id\":13,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"MITTWOCH\"},{\"id\":14,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":15,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"FREITAG\"}],\"officeWorkers\":[{\"id\":5,\"uid\":\"Ea3D4hYZjAU8UNMM3JYpwACMI303\",\"preTitle\":\"\",\"firstName\":\"Manuel\",\"lastName\":\"Meister\",\"posTitle\":\"Dr.\",\"eMail\":\"manuel.meister@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":5,\"specialityName\":\"Technician\"}],\"type\":\"DOCTOR\"}]}]",
                content);
    }

    @Test
    public void findOfficeBySearchText_shouldFindOneOfficesBySpeciality() throws Exception{
        String uri = URI_FIND_OFFICE_BY_SEARCH_TEXT + "Dentist";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[{\"id\":2,\"name\":\"Praxis Dr. Hubert\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":2,\"street\":\"Loewengasse\",\"number\":\"5\",\"door\":\"7\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1030,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"dagobert@duck.eh\",\"officehours\":[{\"id\":5,\"beginTime\":\"09:00\",\"endTime\":\"12:00\",\"daytype\":\"DIENSTAG\"},{\"id\":6,\"beginTime\":\"14:00\",\"endTime\":\"17:00\",\"daytype\":\"DIENSTAG\"},{\"id\":7,\"beginTime\":\"09:30\",\"endTime\":\"13:30\",\"daytype\":\"MITTWOCH\"},{\"id\":8,\"beginTime\":\"08:00\",\"endTime\":\"12:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":9,\"beginTime\":\"14:00\",\"endTime\":\"16:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":10,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"FREITAG\"}],\"officeWorkers\":[{\"id\":3,\"uid\":\"PmgPFW3CsZXht2IuOFiAqldozVV2\",\"preTitle\":\"MSc\",\"firstName\":\"Berta\",\"lastName\":\"Hadel\",\"posTitle\":\"Dr\",\"eMail\":\"berta.hadel@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":3,\"specialityName\":\"Dentist\"}],\"type\":\"DOCTOR\"},{\"id\":4,\"uid\":\"igi3pmYNRRWeQurhojiX8fDJmGj2\",\"preTitle\":\"\",\"firstName\":\"Mist\",\"lastName\":\"Eria\",\"posTitle\":\"Dipl.Ing\",\"eMail\":\"mist.eria@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":4,\"specialityName\":\"Common Allergic\"}],\"type\":\"ASSISTANT\"}]}]",
                content);
    }

    @Test
    public void findOfficeBySearchText_shouldFindNoResult() throws Exception{
        String uri = URI_FIND_OFFICE_BY_SEARCH_TEXT + "Herberito";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[]",
                content);
    }

    @Test
    public void findOfficeBySearchTextNameOnly_shouldFind1Office() throws Exception{
        String uri = URI_FIND_OFFICE_BY_SEARCH_TEXT_NAME_ONLY + "Mayer";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]}]",
                content);
    }

    @Test
    public void findOfficeBySearchTextNameOnly_shouldFindNoResult() throws Exception{
        String uri = URI_FIND_OFFICE_BY_SEARCH_TEXT_NAME_ONLY + "Kapfen";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[]",
                content);
    }

    @Test
    public void findOfficeBySearchTextSpecialityOnly_shouldFind1Office() throws Exception{
        String uri = URI_FIND_OFFICE_BY_SEARCH_TEXT_SPECIALITY_ONLY + "Dentist";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[{\"id\":2,\"name\":\"Praxis Dr. Hubert\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":2,\"street\":\"Loewengasse\",\"number\":\"5\",\"door\":\"7\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1030,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"dagobert@duck.eh\",\"officehours\":[{\"id\":5,\"beginTime\":\"09:00\",\"endTime\":\"12:00\",\"daytype\":\"DIENSTAG\"},{\"id\":6,\"beginTime\":\"14:00\",\"endTime\":\"17:00\",\"daytype\":\"DIENSTAG\"},{\"id\":7,\"beginTime\":\"09:30\",\"endTime\":\"13:30\",\"daytype\":\"MITTWOCH\"},{\"id\":8,\"beginTime\":\"08:00\",\"endTime\":\"12:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":9,\"beginTime\":\"14:00\",\"endTime\":\"16:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":10,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"FREITAG\"}],\"officeWorkers\":[{\"id\":3,\"uid\":\"PmgPFW3CsZXht2IuOFiAqldozVV2\",\"preTitle\":\"MSc\",\"firstName\":\"Berta\",\"lastName\":\"Hadel\",\"posTitle\":\"Dr\",\"eMail\":\"berta.hadel@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":3,\"specialityName\":\"Dentist\"}],\"type\":\"DOCTOR\"},{\"id\":4,\"uid\":\"igi3pmYNRRWeQurhojiX8fDJmGj2\",\"preTitle\":\"\",\"firstName\":\"Mist\",\"lastName\":\"Eria\",\"posTitle\":\"Dipl.Ing\",\"eMail\":\"mist.eria@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":4,\"specialityName\":\"Common Allergic\"}],\"type\":\"ASSISTANT\"}]}]",
                content);
    }

    @Test
    public void findOfficeBySearchTextSpecialityOnly_shouldFindNoResult() throws Exception{
        String uri = URI_FIND_OFFICE_BY_SEARCH_TEXT_SPECIALITY_ONLY + "Bieber";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[]",
                content);
    }

    @Test
    public void findOfficeBySearchTextDoctorNameOnly_shouldFind1Office() throws Exception{
        String uri = URI_FIND_OFFICE_BY_SEARCH_TEXT_DOCTOR_NAME_ONLY + "Hadel";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[{\"id\":2,\"name\":\"Praxis Dr. Hubert\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":2,\"street\":\"Loewengasse\",\"number\":\"5\",\"door\":\"7\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1030,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"dagobert@duck.eh\",\"officehours\":[{\"id\":5,\"beginTime\":\"09:00\",\"endTime\":\"12:00\",\"daytype\":\"DIENSTAG\"},{\"id\":6,\"beginTime\":\"14:00\",\"endTime\":\"17:00\",\"daytype\":\"DIENSTAG\"},{\"id\":7,\"beginTime\":\"09:30\",\"endTime\":\"13:30\",\"daytype\":\"MITTWOCH\"},{\"id\":8,\"beginTime\":\"08:00\",\"endTime\":\"12:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":9,\"beginTime\":\"14:00\",\"endTime\":\"16:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":10,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"FREITAG\"}],\"officeWorkers\":[{\"id\":3,\"uid\":\"PmgPFW3CsZXht2IuOFiAqldozVV2\",\"preTitle\":\"MSc\",\"firstName\":\"Berta\",\"lastName\":\"Hadel\",\"posTitle\":\"Dr\",\"eMail\":\"berta.hadel@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":3,\"specialityName\":\"Dentist\"}],\"type\":\"DOCTOR\"},{\"id\":4,\"uid\":\"igi3pmYNRRWeQurhojiX8fDJmGj2\",\"preTitle\":\"\",\"firstName\":\"Mist\",\"lastName\":\"Eria\",\"posTitle\":\"Dipl.Ing\",\"eMail\":\"mist.eria@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":4,\"specialityName\":\"Common Allergic\"}],\"type\":\"ASSISTANT\"}]}]",
                content);
    }

    @Test
    public void findOfficeBySearchTextDoctorNameOnly_shouldFindNoResult() throws Exception{
        String uri = URI_FIND_OFFICE_BY_SEARCH_TEXT_DOCTOR_NAME_ONLY + "Xerxes";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[]",
                content);
    }

    @Test
    public void findOfficeByMedicalWorker_shouldFindOfficeByMedicalWorker() throws Exception {
        String uri = URI_FIND_OFFICE_BY_MEDICAL_WORKER_ID + 1;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]}",
                content);
    }

    @Test
    public void findOfficeByMedicalWorker_shouldThrowResourceNotFoundExceptionWithCode404() throws Exception {
        String uri = URI_FIND_OFFICE_BY_MEDICAL_WORKER_ID + 15874;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(ResourceNotFoundException.class,
                mvcResult.getResolvedException().getClass());
    }

    @Test
    public void findOfficeByMedicalWorker_shouldThrowMethodArgumentTypeMismatchException() throws Exception {
        String uri = URI_FIND_OFFICE_BY_MEDICAL_WORKER_ID + "abcd";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
        assertEquals(MethodArgumentTypeMismatchException.class,
                mvcResult.getResolvedException().getClass());
    }

    @Test
    public void getPatientOffices_shouldReturnAllOfficesOfPatient() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_2);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_GET_PATIENT_OFFICES)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("[{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},{\"id\":2,\"name\":\"Praxis Dr. Hubert\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":2,\"street\":\"Loewengasse\",\"number\":\"5\",\"door\":\"7\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1030,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"dagobert@duck.eh\",\"officehours\":[{\"id\":5,\"beginTime\":\"09:00\",\"endTime\":\"12:00\",\"daytype\":\"DIENSTAG\"},{\"id\":6,\"beginTime\":\"14:00\",\"endTime\":\"17:00\",\"daytype\":\"DIENSTAG\"},{\"id\":7,\"beginTime\":\"09:30\",\"endTime\":\"13:30\",\"daytype\":\"MITTWOCH\"},{\"id\":8,\"beginTime\":\"08:00\",\"endTime\":\"12:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":9,\"beginTime\":\"14:00\",\"endTime\":\"16:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":10,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"FREITAG\"}],\"officeWorkers\":[{\"id\":3,\"uid\":\"PmgPFW3CsZXht2IuOFiAqldozVV2\",\"preTitle\":\"MSc\",\"firstName\":\"Berta\",\"lastName\":\"Hadel\",\"posTitle\":\"Dr\",\"eMail\":\"berta.hadel@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":3,\"specialityName\":\"Dentist\"}],\"type\":\"DOCTOR\"},{\"id\":4,\"uid\":\"igi3pmYNRRWeQurhojiX8fDJmGj2\",\"preTitle\":\"\",\"firstName\":\"Mist\",\"lastName\":\"Eria\",\"posTitle\":\"Dipl.Ing\",\"eMail\":\"mist.eria@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":4,\"specialityName\":\"Common Allergic\"}],\"type\":\"ASSISTANT\"}]},{\"id\":3,\"name\":\"Praxis Dr. Hubert\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":3,\"street\":\"Herzgasse\",\"number\":\"10\",\"door\":\"6\",\"floor\":\"6\",\"place\":\"Vienna\",\"zip\":1100,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"psycho@farma.fa\",\"officehours\":[{\"id\":11,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"MONTAG\"},{\"id\":12,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"DIENSTAG\"},{\"id\":13,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"MITTWOCH\"},{\"id\":14,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":15,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"FREITAG\"}],\"officeWorkers\":[{\"id\":5,\"uid\":\"Ea3D4hYZjAU8UNMM3JYpwACMI303\",\"preTitle\":\"\",\"firstName\":\"Manuel\",\"lastName\":\"Meister\",\"posTitle\":\"Dr.\",\"eMail\":\"manuel.meister@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":5,\"specialityName\":\"Technician\"}],\"type\":\"DOCTOR\"}]}]"
            ,content);
    }

    @Test
    public void getPatientOffices_shouldReturnEmptyListIfPatientHasNoOffices() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_3);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_GET_PATIENT_OFFICES)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("[]",content);
    }

    @Test
    public void getOfficeByMedicalWorkerLoggedIn() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_MEDICALWORKER_WITH_ID_1);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_GET_MEDICALWORKER_OFFICES)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        OfficeDTO medOffice = super.mapFromJson(content,OfficeDTO.class);

        assertNotNull(medOffice);

        mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_FIND_OFFICE_BY_ID + 1)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        content = mvcResult.getResponse().getContentAsString();

        OfficeDTO compare = super.mapFromJson(content,OfficeDTO.class);

        assertNotNull(compare);

        assertEquals(medOffice, compare);

    }

    @Test
    public void getOfficeByMedicalWorkerLoggedIn_shouldThrowUserNotAuthenticatedException() throws Exception {
        Mockito.when(authentication.getName()).thenReturn("sadkljsdag");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_GET_MEDICALWORKER_OFFICES)).andReturn();

        assertEquals(401, mvcResult.getResponse().getStatus());
        assertEquals(UserNotAuthenticatedException.class,mvcResult.getResolvedException().getClass());
    }

    @Test
    public void getPatientsToLoggedInOffice() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_MEDICALWORKER_WITH_ID_1);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_GET_PATIENTS_OF_OFFICE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        List<PatientDTO> patientOffice = Arrays.asList(super.mapFromJson(content,PatientDTO[].class));

        assertNotNull(patientOffice);
        assertTrue(patientOffice.size() == 2);
        assertEquals(content,"[{\"id\":1,\"uid\":\"1qxo6HiFksd7UYXXY9gtcaGKYCu2\",\"preTitle\":\"Dr.\",\"firstName\":\"Hans\",\"lastName\":\"Mueller\",\"posTitle\":\"\",\"eMail\":\"hans@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"1111111111\"},{\"id\":2,\"uid\":\"wCjBKGC7EMZo49VTMCDb92toPXf2\",\"preTitle\":\"\",\"firstName\":\"Franz\",\"lastName\":\"Walder\",\"posTitle\":\"Bsc.\",\"eMail\":\"franz@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"2222111111\"}]");
    }
    @Test
    public void getUnconfirmedPatients_shouldThrowResourceNotFoundException() throws Exception{
        String uri = URI_FIND_OFFICE_BY_ID + 17 + "/patients/unconfirmed";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void getUnconfirmedPatients_shouldReturnEmptyArray() throws Exception{
        String uri = URI_FIND_OFFICE_BY_ID + 1 + "/patients/unconfirmed";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[]",content);
    }

    @Test
    public void getUnconfirmedPatients_shouldReturn1Patient() throws Exception{
        String uri = URI_FIND_OFFICE_BY_ID + 3 + "/patients/unconfirmed";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[{\"id\":3,\"uid\":\"9VB82zVsNLaOiKDOuRfS6VmLzwx2\",\"preTitle\":\"\",\"firstName\":\"Maria\",\"lastName\":\"Zotter\",\"posTitle\":\"\",\"eMail\":\"maria@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"3333111111\"}]",content);
    }

    @Test
    public void getUnconfirmedPatients_shouldReturn2Patients() throws Exception{
        String uri = URI_FIND_OFFICE_BY_ID + 4 + "/patients/unconfirmed";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[{\"id\":2,\"uid\":\"wCjBKGC7EMZo49VTMCDb92toPXf2\",\"preTitle\":\"\",\"firstName\":\"Franz\",\"lastName\":\"Walder\",\"posTitle\":\"Bsc.\",\"eMail\":\"franz@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"2222111111\"},{\"id\":3,\"uid\":\"9VB82zVsNLaOiKDOuRfS6VmLzwx2\",\"preTitle\":\"\",\"firstName\":\"Maria\",\"lastName\":\"Zotter\",\"posTitle\":\"\",\"eMail\":\"maria@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"3333111111\"}]",content);
    }
    @Test
    public void deleteAppointmentsByOfficeANDPatient_shouldReturn404DueToOffice() throws Exception {
        String uri = URI_FIND_OFFICE_BY_ID + 37 + "/patient/" + 1;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void deleteAppointmentsByOfficeANDPatient_shouldReturn404DueToPatient() throws Exception {
        String uri = URI_FIND_OFFICE_BY_ID + 1 + "/patient/" + 37;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void deleteAppointmentsByOfficeANDPatient_shouldDeleteOneAppointment() throws Exception {
        String uri = URI_FIND_OFFICE_BY_ID + 3 + "/patient/" + 3;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        uri = URI_FIND_OFFICE_BY_ID + 3 + "/patients/unconfirmed";
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals("[]",content);
    }
    @Test
    public void getRegistrationCode_shouldReturnResourceNotFound() throws Exception {
        Mockito.when(authentication.getName()).thenReturn("2");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_GET_REGISTRATIONCODE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void getRegistrationCode_shouldReturnNewRegistrationCode() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_1);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_GET_REGISTRATIONCODE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        RegistrationCodeDTO codeDTO = super.mapFromJson(content,RegistrationCodeDTO.class);
        assertTrue(codeDTO.getCode() >= 1000 && codeDTO.getCode() < 10000);

    }

    @Test
    public void addOffice_shouldReturn404DueToOffice() throws Exception {
        String uri = URI_FIND_OFFICE_BY_ID + 37 + "/patient/" + 3;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}")).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void addOffice_shouldReturn404DueToPatient() throws Exception {
        String uri = URI_FIND_OFFICE_BY_ID + 3 + "/patient/" + 37;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}")).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void addOffice_shouldReturn404DueToMissingRegistrationCode() throws Exception {
        RegistrationCodeDTO codeDTO = new RegistrationCodeDTO();
        String inputJson = super.mapToJson(codeDTO);

        String uri = URI_FIND_OFFICE_BY_ID + 3 + "/patient/" + 3;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void addOffice_shouldReturn400DueToWrongRegistrationCode() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_3);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_GET_REGISTRATIONCODE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        RegistrationCodeDTO codeDTO = new RegistrationCodeDTO();
        codeDTO.setCode(1);
        String inputJson = super.mapToJson(codeDTO);
        String uri = URI_FIND_OFFICE_BY_ID + 3 + "/patient/" + 3;
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        assertEquals("Der Code ist invalid. Bitte versuchen Sie es erneut.",mvcResult.getResponse().getErrorMessage());
    }




    @Test
    public void addOffice_shouldAddOnePatient() throws Exception {
        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_3);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_GET_REGISTRATIONCODE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        RegistrationCodeDTO codeDTO = super.mapFromJson(content,RegistrationCodeDTO.class);
        String inputJson = super.mapToJson(codeDTO);

        String uri = URI_FIND_OFFICE_BY_ID + 3 + "/patient/" + 3;
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        uri = URI_FIND_OFFICE_BY_ID + 3 + "/patients/unconfirmed";
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        content = mvcResult.getResponse().getContentAsString();

        assertEquals("[]",content);

        Mockito.when(authentication.getName()).thenReturn(UID_FOR_PATIENT_WITH_ID_3);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_GET_PATIENT_OFFICES)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        content = mvcResult.getResponse().getContentAsString();
        assertEquals("[{\"id\":3,\"name\":\"Praxis Dr. Hubert\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":3,\"street\":\"Herzgasse\",\"number\":\"10\",\"door\":\"6\",\"floor\":\"6\",\"place\":\"Vienna\",\"zip\":1100,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"psycho@farma.fa\",\"officehours\":[{\"id\":11,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"MONTAG\"},{\"id\":12,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"DIENSTAG\"},{\"id\":13,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"MITTWOCH\"},{\"id\":14,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"DONNERSTAG\"},{\"id\":15,\"beginTime\":\"08:00\",\"endTime\":\"13:00\",\"daytype\":\"FREITAG\"}],\"officeWorkers\":[{\"id\":5,\"uid\":\"Ea3D4hYZjAU8UNMM3JYpwACMI303\",\"preTitle\":\"\",\"firstName\":\"Manuel\",\"lastName\":\"Meister\",\"posTitle\":\"Dr.\",\"eMail\":\"manuel.meister@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":5,\"specialityName\":\"Technician\"}],\"type\":\"DOCTOR\"}]}]",content);


    }


}
