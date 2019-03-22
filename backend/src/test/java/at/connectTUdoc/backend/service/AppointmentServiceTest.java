package at.connectTUdoc.backend.service;

import at.connectTUdoc.backend.dao.*;
import at.docTUconnectr.backend.dao.*;
import at.medconnect.backend.dao.*;
import at.ws18_ase_qse_03.backend.dao.*;
import at.connectTUdoc.backend.dto.AppointmentDTO;
import at.connectTUdoc.backend.model.Appointment;
import at.connectTUdoc.backend.service.impl.AppointmentServiceImpl;
import at.connectTUdoc.backend.service.impl.OfficeServiceImpl;
import at.connectTUdoc.backend.service.impl.PatientServiceImpl;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Assert;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Tests for CRUD operations are no benefit, cause there is no ongoing manipulation threw the service-layer.
 * For example some methods are shown to show how to mock these methods.
 */
@RunWith(SpringRunner.class)
public class AppointmentServiceTest {
    @MockBean
    MedicalWorkerRepository medicalWorkerRepository;

    @MockBean
    private SpecialityRepository specialityRepository;

    @MockBean
    private FirebaseService firebaseService;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    ChatThreadRepository chatThreadRepository;


    @TestConfiguration
    static class AppointmentServiceTestContextConfiguration {
        @Bean
        public OfficeService officeService() {
            return new OfficeServiceImpl();
        }

        @Bean
        public PatientService patientService() {
            return new PatientServiceImpl();
        }


        @Bean
        public AppointmentService appointmentService() {
            return new AppointmentServiceImpl();
        }

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }

    @Autowired
    AppointmentService appointmentService;


    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private OfficeRepository officeRepository;

    @Autowired
    ModelMapper modelMapper;


    @Test
    public void deleteAppointmentById_shouldDeleteAppointmentWithId() {
        Mockito.doNothing().when(appointmentRepository).deleteById(1L);

        appointmentService.deleteAppointmentById(1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getAppointmentsBetweenDatesByOffice_shouldThrowResourceNotFoundException() {
        Mockito.when(officeRepository.findById(27L))
                .thenReturn(Optional.empty());

        this.appointmentService.getAppointmentBetweenDatesByOffice(27L, "1.12.2018","28.02.2019");
    }

    @Test
    public void getAppointmentsBetweenDateByOffice_shourldReturnAnAppointment() {
        Appointment appointment = new Appointment();
        appointment.setId(6L);
        appointment.setAppointmentBegin(LocalDateTime.of(2017,2,11,11,11));
        appointment.setAppointmentEnd(LocalDateTime.of(2017,2,11,12,11));

        Mockito.when(officeRepository.findById(5L))
                .thenReturn(Optional.of(TestUtilsMedConnect.getSingleOffice()));
        Mockito.when(appointmentRepository.getAppointmentsBetweenDatesByOffice(TestUtilsMedConnect.getSingleOffice(),LocalDateTime.of(2017,2,1,0,0,0,0),LocalDateTime.of(2017,2,15,23,59,59,999999999))).
                thenReturn(List.of(appointment));

        List<AppointmentDTO> appointments = this.appointmentService.getAppointmentBetweenDatesByOffice(5L, "01-02-2017","15-02-2017");
        Assert.assertEquals(1,appointments.size());
        Assert.assertEquals(Long.valueOf(6L),appointments.get(0).getId());

        Assert.assertEquals(LocalDateTime.of(2017,2,11,11,11),appointments.get(0).getAppointmentBegin());
        Assert.assertEquals(LocalDateTime.of(2017,2,11,12,11),appointments.get(0).getAppointmentEnd());

    }

    @Test
    public void getAppointmentsBetweenDateByOffice_shouldReturnEmptyList() {
        Appointment appointment = new Appointment();
        appointment.setId(6L);
        appointment.setAppointmentBegin(LocalDateTime.of(2017,2,11,11,11));
        appointment.setAppointmentEnd(LocalDateTime.of(2017,2,11,12,11));

        Mockito.when(officeRepository.findById(5L))
                .thenReturn(Optional.of(TestUtilsMedConnect.getSingleOffice()));
        Mockito.when(appointmentRepository.getAppointmentsBetweenDatesByOffice(TestUtilsMedConnect.getSingleOffice(),LocalDateTime.of(2017,2,1,12,0),LocalDateTime.of(2017,2,15,11,0))).
                thenReturn(new ArrayList<Appointment>());

        List<AppointmentDTO> appointments =  this.appointmentService.getAppointmentBetweenDatesByOffice(5L, "01-02-2017","15-02-2017");
        Assert.assertEquals(0,appointments.size());
    }

}
