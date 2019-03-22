package at.connectTUdoc.backend.service.impl;

import at.connectTUdoc.backend.dao.AppointmentRepository;
import at.connectTUdoc.backend.dao.OfficeRepository;
import at.connectTUdoc.backend.dto.AppointmentDTO;
import at.connectTUdoc.backend.dto.OfficeDTO;
import at.connectTUdoc.backend.dto.PatientDTO;
import at.connectTUdoc.backend.exception.InvalidAppointmentException;
import at.connectTUdoc.backend.model.*;
import at.connectTUdoc.backend.service.AppointmentService;
import at.connectTUdoc.backend.service.PatientService;
import at.docTUconnectr.backend.model.*;
import at.medconnect.backend.model.*;
import at.ws18_ase_qse_03.backend.dto.*;
import at.ws18_ase_qse_03.backend.model.*;
import com.google.firebase.FirebaseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger LOGGER = Logger.getLogger(AppointmentServiceImpl.class.getName());

    @Autowired
    private PatientService patientService;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private OfficeServiceImpl officeService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<AppointmentDTO> findAppointmentsByOffice(Office office) {
        return Arrays.asList(modelMapper.map(appointmentRepository.findAppointmentsByOffice(office), AppointmentDTO[].class));
    }

    @Override
    public List<AppointmentDTO> getAvailableAppointmentsByOfficeAndDate(Long officeId, String date) {
        List<Appointment> availableAppointments = new ArrayList<>();
        Optional<Office> officeOption = officeRepository.findById(officeId);
        if (!officeOption.isPresent()) {
            throw new ResourceNotFoundException("Es wurde versucht, verfügbare Termine zu erhalten, aber kein übereinstimmendes Amt gefunden!");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate localDate = LocalDate.parse(date, formatter);
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        DayType dayType = null;
        switch (dayOfWeek) {
            case MONDAY:
                dayType = DayType.MONTAG;
                break;
            case TUESDAY:
                dayType = DayType.DIENSTAG;
                break;
            case WEDNESDAY:
                dayType = DayType.MITTWOCH;
                break;
            case THURSDAY:
                dayType = DayType.DONNERSTAG;
                break;
            case FRIDAY:
                dayType = DayType.FREITAG;
                break;
        }
        if (dayType == null) {
            return Arrays.asList(modelMapper.map(availableAppointments, AppointmentDTO[].class));
        }
        Office office = officeOption.get();
        List<LocalTime> timeSlotBeginnings = generateTimeSlotsBeginnings(office, dayType); //generate available appointments
        availableAppointments = generateAvailableAppointments(timeSlotBeginnings, office, localDate);
        return Arrays.asList(modelMapper.map(availableAppointments, AppointmentDTO[].class));
    }

    @Override
    public List<AppointmentDTO> getAllAppointmentsOfLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        List<AppointmentDTO> appointmentDTOs = new ArrayList<>();
        PatientDTO patientDTO;
        try {
            patientDTO = patientService.retrieveCurrentPatient(uid);
            Patient patient = modelMapper.map(patientDTO, Patient.class);
            List<Appointment> appointments = appointmentRepository.findAppointmentsByPatient(patient, LocalDateTime.now().withNano(0).withSecond(0));
            appointmentDTOs = Arrays.asList(modelMapper.map(appointments, AppointmentDTO[].class));
        } catch (FirebaseException e) {
            LOGGER.severe("User not authenticated, " + e.getMessage());
        }

        return appointmentDTOs;
    }

    private List<LocalTime> generateTimeSlotsBeginnings(Office office, DayType dayType) {
        List<LocalTime> timeSlotBeginnings = new ArrayList<>();
        for (OfficeHour officeHour : office.getOfficehours()) {
            if (officeHour.getDaytype() == dayType) {
                LocalTime start = officeHour.getBeginTime();
                LocalTime end = officeHour.getEndTime();
                LocalTime currentSlotStart = start;
                timeSlotBeginnings.add(currentSlotStart);
                while (currentSlotStart.plusMinutes(59L).isBefore(end)) {
                    currentSlotStart = currentSlotStart.plusMinutes(30);
                    timeSlotBeginnings.add(currentSlotStart);
                }
            }
        }
        return timeSlotBeginnings;
    }

    private List<Appointment> generateAvailableAppointments(List<LocalTime> timeSlotBeginnings, Office office, LocalDate date) {
        List<Appointment> availableAppointments = new ArrayList<>();
        List<Appointment> takenAppointments = appointmentRepository.findAppointmentsByOfficeAndAppointmentBeginBetween(office, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        Appointment currentAppointment;
        for (LocalTime timeSlotStart : timeSlotBeginnings) {
            boolean appointmentAvailable = true;
            LocalDateTime begin = LocalDateTime.of(date, timeSlotStart);
            LocalDateTime end = LocalDateTime.of(date, timeSlotStart.plusMinutes(30L));
            for (Appointment takenAppointment : takenAppointments) {
                LocalDateTime takenAppoBegin = takenAppointment.getAppointmentBegin();
                LocalDateTime takenAppoEnd = takenAppointment.getAppointmentEnd();
                if (takenAppoBegin.isAfter(begin) && takenAppoBegin.isBefore(end) ||
                        takenAppoEnd.isAfter(begin) && takenAppoEnd.isBefore(end) ||
                        takenAppoBegin.isBefore(begin) && takenAppoEnd.isAfter(end) ||
                        takenAppoBegin.isEqual(begin) || takenAppoEnd.isEqual(end) ) {
                    appointmentAvailable = false;
                }
            }
            if (!appointmentAvailable) {
                continue;
            }
            currentAppointment = new Appointment();
            currentAppointment.setAppointmentBegin(begin);
            currentAppointment.setAppointmentEnd(end);
            currentAppointment.setOffice(office);
            availableAppointments.add(currentAppointment);
        }

        return availableAppointments;
    }

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) throws ResponseStatusException {
        if (appointmentDTO == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Termin ist ungültig!", new IllegalArgumentException());
        }
        if (appointmentDTO.getPatient() == null && StringUtils.isEmpty(appointmentDTO.getPatientName())) {
            LOGGER.info("No patient set for appointment, setting currently authenticated patient...");

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String uid = authentication.getName();

            PatientDTO patientDTO;
            try {
                patientDTO = patientService.retrieveCurrentPatient(uid);
                appointmentDTO.setPatient(patientDTO);
            } catch (FirebaseException e) {
                LOGGER.severe("User not authenticated, " + e.getMessage());
            }
        }
        if(appointmentDTO.getPatient() != null) {
            appointmentDTO.setPatientName(appointmentDTO.getPatient().getFirstName() + " " + appointmentDTO.getPatient().getLastName());
        }

        var appointment = modelMapper.map(appointmentDTO, Appointment.class);

        try {
            validateAppointment(appointment,false);
        } catch (InvalidAppointmentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return modelMapper.map(appointmentRepository.save(appointment), AppointmentDTO.class);
    }

    /**
     * The method verifies if the given appointment is within the office hours of the
     * office.
     * @param appointment appointment to be verified
     * @return if the appointment is within the office hours
     */
    private boolean checkAppointmentInOfficeHour(Appointment appointment) {
        for(OfficeHour officeHour : appointment.getOffice().getOfficehours()) {
            String weekday;
            switch (officeHour.getDaytype()) {
                case MONTAG:
                    weekday = "MONDAY";
                    break;
                case DIENSTAG:
                    weekday = "TUESDAY";
                    break;
                case MITTWOCH:
                    weekday = "WEDNESDAY";
                    break;
                case DONNERSTAG:
                    weekday = "THURSDAY";
                    break;
                case FREITAG:
                    weekday = "FRIDAY";
                    break;
                case SAMSTAG:
                    weekday = "SATURDAY";
                    break;
                case SONNTAG:
                    weekday = "SUNDAY";
                    break;
                default:
                    weekday= "";
                    break;
            }
            LocalTime start = appointment.getAppointmentBegin().toLocalTime();
            LocalTime end = appointment.getAppointmentEnd().toLocalTime();
            if(weekday.equals(appointment.getAppointmentBegin().getDayOfWeek().name()) && weekday.equals(appointment.getAppointmentEnd().getDayOfWeek().name())) {
                if((officeHour.getBeginTime().isBefore(start) || officeHour.getBeginTime().equals(start)) && (officeHour.getEndTime().isAfter(end) || officeHour.getEndTime().equals(end))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * The method validates a given appointment. It is verified that the appointment is in the future and
     * that the end time is after the begin time. It is also verified that the appointment does not overlap
     * with an existing appointment other than itself (update).
     * @param appointment appointment to be validated
     * @param update if an existing appointment is updated or a new appointment is created
     * @throws InvalidAppointmentException if the appointment is not valid
     */
    private void validateAppointment(Appointment appointment, boolean update) throws InvalidAppointmentException {
        if(!appointment.getAppointmentBegin().isAfter(LocalDateTime.now().withSecond(0).withNano(0))) {
            throw new InvalidAppointmentException("Termin muss in der Zukunft sein.");
        }
        if(!appointment.getAppointmentEnd().isAfter(appointment.getAppointmentBegin())) {
            throw new InvalidAppointmentException("Terminende muss nach Terminbeginn sein");
        }
        if(!checkAppointmentInOfficeHour(appointment)) {
            throw new InvalidAppointmentException("Termin ist außerhalb der Bürozeiten.");
        }
        List<Appointment> overlapping = null;
        if(update) {
            overlapping = appointmentRepository.getOverlappingAppointmentsByOfficeAndId(appointment.getOffice(), appointment.getId(),appointment.getAppointmentBegin(), appointment.getAppointmentEnd());
        } else {
           overlapping = appointmentRepository.getOverlappingAppointmentsByOffice(appointment.getOffice(), appointment.getAppointmentBegin(), appointment.getAppointmentEnd());
        }

        if(overlapping != null && overlapping.size() > 0) {
            throw new InvalidAppointmentException("Der Termin überschneidet sich mit einem anderen Termin.");
        }

    }


    @Override
    public AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO) throws ResponseStatusException{
        Optional<Appointment> oldAppointment = appointmentRepository.findById(appointmentDTO.getId());
        Appointment appointment = null;
        if(oldAppointment.isPresent()) {
            appointment = oldAppointment.get();
        }
        appointment.setAppointmentBegin(appointmentDTO.getAppointmentBegin());
        appointment.setAppointmentEnd(appointmentDTO.getAppointmentEnd());

        try {
            validateAppointment(appointment,true);
        } catch (InvalidAppointmentException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return modelMapper.map(appointmentRepository.save(appointment), AppointmentDTO.class);
    }

    @Override
    public void deleteAppointmentById(Long id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public List<AppointmentDTO> getAppointmentBetweenDatesByOffice(Long officeId, String startDate, String endDate) {
        OfficeDTO officeDTO = this.officeService.findOfficeById(officeId);
        if(officeDTO == null) {
            throw new ResourceNotFoundException("Versuche, Termine zu bekommen, aber kein passendes Büro gefunden!");
        }
        Office office = modelMapper.map(officeDTO,Office.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDateTime start = LocalDate.parse(startDate, formatter).atTime(0,0,0,0);
        LocalDateTime end = LocalDate.parse(endDate, formatter).atTime(23,59,59,999999999);

        var appointments = appointmentRepository.getAppointmentsBetweenDatesByOffice(office,start,end);
        return Arrays.asList(modelMapper.map(appointments, AppointmentDTO[].class));
    }
}
