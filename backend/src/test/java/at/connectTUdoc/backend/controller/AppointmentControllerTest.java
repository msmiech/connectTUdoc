package at.connectTUdoc.backend.controller;

import at.connectTUdoc.backend.controller.abstracts.AbstractWebTest;
import at.connectTUdoc.backend.dto.AppointmentDTO;
import at.connectTUdoc.backend.dto.OfficeDTO;
import at.connectTUdoc.backend.dto.OfficeHourDTO;
import at.connectTUdoc.backend.model.DayType;
import at.connectTUdoc.backend.service.FirebaseService;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.firebase.auth.UserRecord;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This class hold the controller tests
 */
public class AppointmentControllerTest extends AbstractWebTest {

    private static final String URI_BASIC_APPOINTMENT = "/appointment";
    private static final String URI_APPOINTMENT_BY_OFFICE = "/appointment/office";
    private static final String URI_AVAILABLE_APPOINTMENTS = "/appointment/available/";

    private static final String KNOWN_FIREBASE_USER_UID = "wCjBKGC7EMZo49VTMCDb92toPXf2";
    private static final String KNOWN_FIREBASE_USER_MAIL = "franz@patient.com";
    private static final String KNOWN_FIREBASE_USER_NAME = "Franz Walder";

    protected static final String URI_USER_AUTH = "/patient/current";

    @MockBean
    Authentication authentication;
    @MockBean
    SecurityContext securityContext;
    @MockBean
    FirebaseService firebaseService;

    @MockBean
    private UserRecord userRecord;

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void deleteAppointment_DeletesAppointmentWithId1() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(URI_BASIC_APPOINTMENT + "/" + 1)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void getAppointmentsByOffice_ReturnsAppointmentWithId1() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_APPOINTMENT_BY_OFFICE + "/" + 1 + "?start=01-12-2018&end=28-02-2019")
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String  content = mvcResult.getResponse().getContentAsString();
        assertEquals("[{\"id\":1,\"office\":{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},\"patient\":{\"id\":1,\"uid\":\"1qxo6HiFksd7UYXXY9gtcaGKYCu2\",\"preTitle\":\"Dr.\",\"firstName\":\"Hans\",\"lastName\":\"Mueller\",\"posTitle\":\"\",\"eMail\":\"hans@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"1111111111\"},\"appointmentBegin\":\"2018-12-10 11:00\",\"appointmentEnd\":\"2018-12-10 11:30\",\"patientName\":null},{\"id\":5,\"office\":{\"id\":1,\"name\":\"Praxis Dr. Mayer\",\"phone\":\"125478\",\"fax\":\"02658 741258\",\"address\":{\"id\":1,\"street\":\"Zieglergasse\",\"number\":\"7\",\"door\":\"1\",\"floor\":\"7\",\"place\":\"Vienna\",\"zip\":1070,\"city\":\"Vienna\",\"country\":\"Austria\"},\"email\":\"office@hugo.at\",\"officehours\":[{\"id\":1,\"beginTime\":\"07:00\",\"endTime\":\"12:00\",\"daytype\":\"MONTAG\"},{\"id\":2,\"beginTime\":\"13:00\",\"endTime\":\"16:00\",\"daytype\":\"MONTAG\"},{\"id\":3,\"beginTime\":\"08:00\",\"endTime\":\"11:00\",\"daytype\":\"DIENSTAG\"},{\"id\":4,\"beginTime\":\"13:00\",\"endTime\":\"18:00\",\"daytype\":\"DONNERSTAG\"}],\"officeWorkers\":[{\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynaekologe\"}],\"type\":\"DOCTOR\"},{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"}]},\"patient\":{\"id\":2,\"uid\":\"wCjBKGC7EMZo49VTMCDb92toPXf2\",\"preTitle\":\"\",\"firstName\":\"Franz\",\"lastName\":\"Walder\",\"posTitle\":\"Bsc.\",\"eMail\":\"franz@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"2222111111\"},\"appointmentBegin\":\"2018-12-10 09:30\",\"appointmentEnd\":\"2018-12-10 10:00\",\"patientName\":null}]",content);
    }

    @Test
    public void getAppointmentsByOffice_throwsResourceNotFoundException() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_APPOINTMENT_BY_OFFICE + "/" + 6 + "?start=01-12-2018&end=28-02-2019")
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void getAppointmentsByOffice_ReturnsEmptyArray() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_APPOINTMENT_BY_OFFICE + "/" + 1 + "?start=01-12-2013&end=28-02-2013")
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String  content = mvcResult.getResponse().getContentAsString();
        assertEquals("[]",content);
    }

    @Test
    public void updateAppointment_ErrorDueToAppointmentInThePast() throws Exception {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(1L);
        appointmentDTO.setAppointmentBegin(LocalDateTime.of(2019,1,9,14,0));
        appointmentDTO.setAppointmentEnd(LocalDateTime.of(2019,1,9,14,30));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(URI_BASIC_APPOINTMENT)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(super.mapToJson(appointmentDTO))).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        assertEquals(mvcResult.getResponse().getErrorMessage(), "Termin muss in der Zukunft sein.");
    }

    @Test
    public void updateAppointment_UpdatesAppointment() throws Exception {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(4L);
        appointmentDTO.setAppointmentBegin(LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY)).withHour(14).withMinute(0).withSecond(0).withNano(0));
        appointmentDTO.setAppointmentEnd(appointmentDTO.getAppointmentBegin().plusMinutes(30));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(URI_BASIC_APPOINTMENT)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(super.mapToJson(appointmentDTO))).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String  content = mvcResult.getResponse().getContentAsString();
        AppointmentDTO updatedAppointment = super.mapFromJson(content,AppointmentDTO.class);

        assertEquals(appointmentDTO.getAppointmentBegin(), updatedAppointment.getAppointmentBegin());
        assertEquals(appointmentDTO.getAppointmentEnd(), updatedAppointment.getAppointmentEnd());

    }

    @Test
    public void postSignupForAppointment_ErrorDueToAppointmentInThePast() throws Exception {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setOffice(TestUtilsMedConnect.getSingleOfficeDTO());
        appointmentDTO.setPatientName("Max Musterman");
        appointmentDTO.setAppointmentBegin(LocalDateTime.of(2019,1,9,14,0));
        appointmentDTO.setAppointmentEnd(LocalDateTime.of(2019,1,9,14,30));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URI_BASIC_APPOINTMENT)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(super.mapToJson(appointmentDTO))).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        assertEquals(mvcResult.getResponse().getErrorMessage(), "Termin muss in der Zukunft sein.");
    }

    @Test
    public void postSignupForAppointment_CreatesNewAppointment() throws Exception {
        OfficeDTO officeDTO = new OfficeDTO();
        List<OfficeHourDTO> officehours = new ArrayList<>();
        officeDTO.setId(4L);

        OfficeHourDTO officeHourDTO1 = new OfficeHourDTO();
        officeHourDTO1.setId(16L);
        officeHourDTO1.setDaytype(DayType.DIENSTAG);
        officeHourDTO1.setBeginTime(LocalTime.of(9,0));
        officeHourDTO1.setEndTime(LocalTime.of(13,0));
        officehours.add(officeHourDTO1);
        OfficeHourDTO officeHourDTO2 = new OfficeHourDTO();
        officeHourDTO2.setId(17L);
        officeHourDTO2.setDaytype(DayType.MITTWOCH);
        officeHourDTO2.setBeginTime(LocalTime.of(9,0));
        officeHourDTO2.setEndTime(LocalTime.of(12,0));
        officehours.add(officeHourDTO2);
        OfficeHourDTO officeHourDTO3 = new OfficeHourDTO();
        officeHourDTO3.setId(18L);
        officeHourDTO3.setDaytype(DayType.MITTWOCH);
        officeHourDTO3.setBeginTime(LocalTime.of(14,0));
        officeHourDTO3.setEndTime(LocalTime.of(18,0));
        officehours.add(officeHourDTO3);
        officeDTO.setOfficehours(officehours);

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setOffice(officeDTO);
        appointmentDTO.setPatientName("Max Musterman");
        appointmentDTO.setAppointmentBegin(LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY)).withHour(14).withMinute(0).withSecond(0).withNano(0));
        appointmentDTO.setAppointmentEnd(appointmentDTO.getAppointmentBegin().plusMinutes(30));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URI_BASIC_APPOINTMENT)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(super.mapToJson(appointmentDTO))).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String  content = mvcResult.getResponse().getContentAsString();
        assertEquals("true",content);

    }
    @Test
    public void getAllAvailableAppointmentsByDateAndOffice_ThrowsResourceNotFoundException() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_AVAILABLE_APPOINTMENTS + 7 + "/09-01-2018")).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void getAllAvailableAppointmentsByDateAndOffice_EmptyArrayOutsideOfficeHours() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_AVAILABLE_APPOINTMENTS + 4 + "/10-01-2019")).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String  content = mvcResult.getResponse().getContentAsString();
        assertEquals("[]",content);
    }

    @Test
    public void getAllAvailableAppointmentsByDateAndOffice_ReturnsArrayOfAvailableAppointments() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_AVAILABLE_APPOINTMENTS + 4 + "/12-12-2018")).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String  content = mvcResult.getResponse().getContentAsString();
        List<AppointmentDTO> appointments = super.objectMapper.readValue(content, new TypeReference<List<AppointmentDTO>>(){});
        assertEquals(13,appointments.size());
        assertEquals(LocalDateTime.of(2018,12,12,9,0), appointments.get(0).getAppointmentBegin());
        assertEquals(LocalDateTime.of(2018,12,12,9,30), appointments.get(0).getAppointmentEnd());
        assertEquals(LocalDateTime.of(2018,12,12,9,30), appointments.get(1).getAppointmentBegin());
        assertEquals(LocalDateTime.of(2018,12,12,10,0), appointments.get(1).getAppointmentEnd());
        assertEquals(LocalDateTime.of(2018,12,12,10,0), appointments.get(2).getAppointmentBegin());
        assertEquals(LocalDateTime.of(2018,12,12,10,30), appointments.get(2).getAppointmentEnd());
        assertEquals(LocalDateTime.of(2018,12,12,10,30), appointments.get(3).getAppointmentBegin());
        assertEquals(LocalDateTime.of(2018,12,12,11,0), appointments.get(3).getAppointmentEnd());
        assertEquals(LocalDateTime.of(2018,12,12,11,0), appointments.get(4).getAppointmentBegin());
        assertEquals(LocalDateTime.of(2018,12,12,11,30), appointments.get(4).getAppointmentEnd());
        assertEquals(LocalDateTime.of(2018,12,12,11,30), appointments.get(5).getAppointmentBegin());
        assertEquals(LocalDateTime.of(2018,12,12,12,0), appointments.get(5).getAppointmentEnd());
        assertEquals(LocalDateTime.of(2018,12,12,14,0), appointments.get(6).getAppointmentBegin());
        assertEquals(LocalDateTime.of(2018,12,12,14,30), appointments.get(6).getAppointmentEnd());
        assertEquals(LocalDateTime.of(2018,12,12,14,30), appointments.get(7).getAppointmentBegin());
        assertEquals(LocalDateTime.of(2018,12,12,15,0), appointments.get(7).getAppointmentEnd());
        assertEquals(LocalDateTime.of(2018,12,12,15,0), appointments.get(8).getAppointmentBegin());
        assertEquals(LocalDateTime.of(2018,12,12,15,30), appointments.get(8).getAppointmentEnd());
        assertEquals(LocalDateTime.of(2018,12,12,16,0), appointments.get(9).getAppointmentBegin());
        assertEquals(LocalDateTime.of(2018,12,12,16,30), appointments.get(9).getAppointmentEnd());
        assertEquals(LocalDateTime.of(2018,12,12,16,30), appointments.get(10).getAppointmentBegin());
        assertEquals(LocalDateTime.of(2018,12,12,17,0), appointments.get(10).getAppointmentEnd());
        assertEquals(LocalDateTime.of(2018,12,12,17,0), appointments.get(11).getAppointmentBegin());
        assertEquals(LocalDateTime.of(2018,12,12,17,30), appointments.get(11).getAppointmentEnd());
        assertEquals(LocalDateTime.of(2018,12,12,17,30), appointments.get(12).getAppointmentBegin());
        assertEquals(LocalDateTime.of(2018,12,12,18,0), appointments.get(12).getAppointmentEnd());
    }
}
