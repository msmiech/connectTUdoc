package at.connectTUdoc.backend.repository;

import at.connectTUdoc.backend.dao.AppointmentRepository;
import at.connectTUdoc.backend.dao.OfficeRepository;
import at.connectTUdoc.backend.dao.PatientRepository;
import at.connectTUdoc.backend.model.Appointment;
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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * This class contains the test cases for the repository tests for the specific class and the database transactions
 */
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest(showSql = false)
public class AppointmentRepositoryTest {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    TestUtilsMedConnect testUtilsMedConnect;

    @TestConfiguration
    static class AppointmentRepositoryTestContextConfiguration {

        @Bean
        public TestUtilsMedConnect testUtilsMedConnect() {
            return new TestUtilsMedConnect();
        }
    }

    @Before
    public void setUP() {
    }

    @Test
    public void findAppointmentById_shouldReturnAppointment() {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(1L);
        assertTrue(optionalAppointment.isPresent());
    }

    @Test
    public void findAppointmentsByOfficeAndDate_shouldReturnTwoAppointments() {
        Optional<Office> officeOptional = officeRepository.findById(1L);
        Office office = officeOptional.get();
        List<Appointment> appointments = appointmentRepository.findAppointmentsByOfficeAndAppointmentBeginBetween(office,
                LocalDateTime.of(2018, 12, 10, 0, 0),
                LocalDateTime.of(2018, 12, 10, 23, 59));
        assertEquals(2, appointments.size());
        Appointment appointmentResult1 = appointments.get(0);
        assertEquals(10, appointmentResult1.getAppointmentBegin().getDayOfMonth());
        assertEquals(12, appointmentResult1.getAppointmentBegin().getMonthValue());
        assertEquals(2018, appointmentResult1.getAppointmentBegin().getYear());
    }

    // ------------ Vanessa Repo Test starts here --------------------

    @Test
    public void deleteAppointmentById_deletesAppointmentWithExistingId() {
        Appointment toDelete = appointmentRepository.findById(3L).orElse(null);
        assertNotNull(toDelete);
        appointmentRepository.deleteById(3L);
        Appointment isDeleted = appointmentRepository.findById(3L).orElse(null);
        Assert.assertNull(isDeleted);

    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteAppointmentById_shouldThrowExceptionForNotExistingAppointment() {
        appointmentRepository.deleteById(0L);
    }

    @Test
    public void saveAppointment_shouldSaveNewAppointment() {
        Appointment appointment = new Appointment();

        Office office = officeRepository.getOne(1L);

        appointment.setId(2L);
        appointment.setAppointmentBegin(LocalDateTime.of(2018, 12, 15, 8, 0));
        appointment.setAppointmentBegin(LocalDateTime.of(2018, 12, 15, 8, 30));
        appointment.setPatientName("Test Patient 1");
        appointment.setOffice(office);

        Appointment saved = appointmentRepository.save(appointment);

        assertEquals(appointment.getId(), saved.getId());

        assertEquals(appointment.getId(), saved.getId());
        assertEquals(appointment.getAppointmentBegin(), saved.getAppointmentBegin());
        assertEquals(appointment.getAppointmentEnd(), saved.getAppointmentEnd());
        assertEquals(appointment.getPatientName(), saved.getPatientName());
        assertEquals(appointment.getOffice().getName(), saved.getOffice().getName());
    }

    @Test
    public void updateAppointment_shouldUpdateExistingAppointment() {
        Appointment appointment = appointmentRepository.getOne(3L);

        appointment.setAppointmentBegin(appointment.getAppointmentBegin().plusHours(2));
        appointment.setAppointmentEnd(appointment.getAppointmentEnd().plusHours(2));

        Appointment updated = appointmentRepository.save(appointment);

        assertEquals(appointment.getAppointmentBegin(), updated.getAppointmentBegin());
        assertEquals(appointment.getAppointmentEnd(), updated.getAppointmentEnd());
    }

    @Test
    public void getAppointmentsBetweenDatesByOffice_shouldReturnListOfAppointments() {
        Office office = officeRepository.getOne(1L);
        LocalDateTime start = LocalDateTime.of(2018,12,10,0,0,0);
        LocalDateTime end = LocalDateTime.of(2018,12,10,23,59,59);

        List<Appointment> appointments = appointmentRepository.getAppointmentsBetweenDatesByOffice(office,start,end);
        List<Appointment> expected = List.of(appointmentRepository.getOne(1L),appointmentRepository.getOne(5L));
        assertEquals(expected, appointments);

    }

    @Test
    public void getAppointmentsBetweenDatesByOffice_shouldReturnListOfOneAppointment() {
        Office office = officeRepository.getOne(1L);
        LocalDateTime start = LocalDateTime.of(2018,12,10,9,29,59);
        LocalDateTime end = LocalDateTime.of(2018,12,10,10,59,59);

        List<Appointment> appointments = appointmentRepository.getAppointmentsBetweenDatesByOffice(office,start,end);
        List<Appointment> expected = List.of(appointmentRepository.getOne(5L));
        assertEquals(expected, appointments);

    }
    @Test
    public void getOverlappingAppointmentsByOffice_shouldReturnListOfOneAppointmentOverlappingBeginningAndEnd() {
        Office office = officeRepository.getOne(1L);
        LocalDateTime start = LocalDateTime.of(2018,12,10,10,59);
        LocalDateTime end = LocalDateTime.of(2018,12,10,11,31);

        List<Appointment> appointments = appointmentRepository.getOverlappingAppointmentsByOffice(office,start,end);
        List<Appointment> expected = List.of(appointmentRepository.getOne(1L));
        assertEquals(expected, appointments);
    }

    @Test
    public void getOverlappingAppointmentsByOffice_shouldReturnListOfOneAppointmentOverlappingAtTheBeginning() {
        Office office = officeRepository.getOne(1L);
        LocalDateTime start = LocalDateTime.of(2018,12,10,10,59);
        LocalDateTime end = LocalDateTime.of(2018,12,10,11,01);

        List<Appointment> appointments = appointmentRepository.getOverlappingAppointmentsByOffice(office,start,end);
        List<Appointment> expected = List.of(appointmentRepository.getOne(1L));
        assertEquals(expected, appointments);
    }

    @Test
    public void getOverlappingAppointmentsByOffice_shouldReturnListOfOneAppointmentOverlappingAtTheEnd() {
        Office office = officeRepository.getOne(1L);
        LocalDateTime start = LocalDateTime.of(2018,12,10,11,29);
        LocalDateTime end = LocalDateTime.of(2018,12,10,11,31);

        List<Appointment> appointments = appointmentRepository.getOverlappingAppointmentsByOffice(office,start,end);
        List<Appointment> expected = List.of(appointmentRepository.getOne(1L));
        assertEquals(expected, appointments);
    }

    @Test
    public void getOverlappingAppointmentsByOffice_shouldReturnZeroAppointments() {
        Office office = officeRepository.getOne(1L);
        LocalDateTime start = LocalDateTime.of(2018,12,10,10,0);
        LocalDateTime end = LocalDateTime.of(2018,12,10,11,0);

        List<Appointment> appointments = appointmentRepository.getOverlappingAppointmentsByOffice(office,start,end);
        assertEquals(0, appointments.size());
    }


    @Test
    public void getOverlappingAppointmentsByOfficeAndId_shouldReturnListOfOneAppointmentOverlappingAtTheBeginning() {
        Office office = officeRepository.getOne(1L);
        LocalDateTime start = LocalDateTime.of(2018,12,10,10,30);
        LocalDateTime end = LocalDateTime.of(2018,12,10,11,0);

        Appointment appointment = new Appointment();
        appointment.setPatientName("Max Musterman");
        appointment.setOffice(office);
        appointment.setAppointmentBegin(start);
        appointment.setAppointmentEnd(end);
        appointment = appointmentRepository.save(appointment);

        List<Appointment> appointments = appointmentRepository.getOverlappingAppointmentsByOfficeAndId(office,appointment.getId(),start,end.plusMinutes(1));
        List<Appointment> expected = List.of(appointmentRepository.getOne(1L));
        assertEquals(expected, appointments);
    }

    @Test
    public void getOverlappingAppointmentsByOfficeAndId_shouldReturnListOfOneAppointmentOverlappingAtTheEnd() {
        Office office = officeRepository.getOne(1L);
        LocalDateTime start = LocalDateTime.of(2018,12,10,10,30);
        LocalDateTime end = LocalDateTime.of(2018,12,10,11,0);

        Appointment appointment = new Appointment();
        appointment.setPatientName("Max Musterman");
        appointment.setOffice(office);
        appointment.setAppointmentBegin(start);
        appointment.setAppointmentEnd(end);
        appointment = appointmentRepository.save(appointment);

        List<Appointment> appointments = appointmentRepository.getOverlappingAppointmentsByOfficeAndId(office,appointment.getId(),start.plusMinutes(59),end.plusMinutes(31));
        List<Appointment> expected = List.of(appointmentRepository.getOne(1L));
        assertEquals(expected, appointments);
    }

    @Test
    public void getOverlappingAppointmentsByOfficeAndId_shouldReturnZeroAppointments() {
        Office office = officeRepository.getOne(1L);
        LocalDateTime start = LocalDateTime.of(2018,12,10,10,30);
        LocalDateTime end = LocalDateTime.of(2018,12,10,11,0);

        Appointment appointment = new Appointment();
        appointment.setPatientName("Max Musterman");
        appointment.setOffice(office);
        appointment.setAppointmentBegin(start);
        appointment.setAppointmentEnd(end);
        appointment = appointmentRepository.save(appointment);

        List<Appointment> appointments = appointmentRepository.getOverlappingAppointmentsByOfficeAndId(office,appointment.getId(),start.minusMinutes(1),end);
        assertEquals(0, appointments.size());
    }
    @Test
    public void deleteAppointmentByOfficeAndPatient_shouldDeleteOneAppointment() {
        Office office = officeRepository.getOne(3L);
        Patient patient = patientRepository.getOne(3L);

        assertEquals(2,appointmentRepository.findAppointmentsByOffice(office).size());

        appointmentRepository.deleteAppointmentByOfficeAndPatient(office,patient);

        assertEquals(1,appointmentRepository.findAppointmentsByOffice(office).size());

    }

    @Test
    public void deleteAppointmentByOfficeAndPatient_shouldDeleteZeroAppointments() {
        Office office = officeRepository.getOne(1L);
        Patient patient = patientRepository.getOne(3L);

        assertEquals(2,appointmentRepository.findAppointmentsByOffice(office).size());

        appointmentRepository.deleteAppointmentByOfficeAndPatient(office,patient);

        assertEquals(2,appointmentRepository.findAppointmentsByOffice(office).size());
    }

}
