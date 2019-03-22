package at.connectTUdoc.backend.util;

import at.connectTUdoc.backend.model.*;
import at.docTUconnectr.backend.model.*;
import at.medconnect.backend.model.*;
import at.connectTUdoc.backend.dto.OfficeHourDTO;
import at.ws18_ase_qse_03.backend.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * This class contains methods for generate instances of MedConnect DTO´s for testing purposes
 */
public class DataGeneration {

    @Autowired
    private static ObjectMapper objectMapper;

    public static String mapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public static <T> T mapFromJson(String json, Class<T> clazz)
            throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    /**
     * Sort the office hours by the day of the week
     * @param officeHours - the office hour collections
     */
    public static void sortByWeekDays(List<OfficeHourDTO> officeHours){
        officeHours.sort(Comparator.comparing(OfficeHourDTO::getDaytype));
    }

    private static OfficeHour generateOfficeHour(String beginTime, String endTime, DayType dayType) {
        OfficeHour officeHour = new OfficeHour();
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime beginLocalTime = LocalTime.parse(beginTime, dtfTime);
        LocalTime endLocalTime = LocalTime.parse(endTime, dtfTime);
        officeHour.setBeginTime(beginLocalTime);
        officeHour.setEndTime(endLocalTime);
        officeHour.setDaytype(dayType);
        return officeHour;
    }

    public static Office generateOffice(String name, String email, String street, String number, String floor, String door,
                                        String place, String city, String country, LocalDateTime appointmentDateBegin, LocalDateTime appointmentDateEnd) {
        Random random = new Random();
        Office office = new Office();

        Address address;
        address = new Address();
        address.setCity(city);
        address.setCountry(country);
        address.setDoor(door);
        address.setFloor(floor);
        address.setNumber(number);
        address.setPlace(place);
        address.setStreet(street);
        address.setZip(random.nextInt(10000));

        List<Appointment> appointments;
        appointments = new ArrayList<>();
        Appointment appointment;
        appointment = new Appointment();


        appointment.setAppointmentBegin(appointmentDateBegin);
        appointment.setAppointmentEnd(appointmentDateEnd);
        appointments.add(appointment);

        List<OfficeHour> officeHours;
        officeHours = new ArrayList<>();
        officeHours.add(generateOfficeHour("07:00", "12:00", DayType.MONTAG));
        officeHours.add(generateOfficeHour("13:00", "16:00", DayType.MONTAG));
        officeHours.add(generateOfficeHour("08:00", "11:00", DayType.DIENSTAG));
        officeHours.add(generateOfficeHour("13:00", "18:00", DayType.DONNERSTAG));

        office.setAddress(address);
        office.setAppointments(appointments);
        office.setEmail(email);
        office.setFax(String.format("%08d", random.nextInt(100000000)));
        office.setName(name);
        office.setPhone(String.format("%06d", random.nextInt(1000000)));
        office.setOfficeHours(officeHours);

        office.setOfficeWorkers(new ArrayList<>());
        return office;
    }

    public static MedicalWorker generateMedicalWorker(String uid, String firstName, String lastName, String priTitle, String postTitle, MedWorkerType medWorkerType, String specialityName, String email) {
        Random random = new Random();
        MedicalWorker med1;
        med1 = new MedicalWorker();

        ArrayList<Speciality> specialities = new ArrayList<>();
        specialities.clear();

        Speciality spHuman;
        spHuman = new Speciality();
        spHuman.setSpecialityName(specialityName);
        specialities.add(spHuman);

        med1.setUid(uid);
        med1.seteMail(email);
        med1.setType(medWorkerType);
        med1.setSpecialities(specialities);
        med1.setFirstName(firstName);
        med1.setLastName(lastName);
        med1.setPosTitle(postTitle);
        med1.setPreTitle(priTitle);
        med1.setPrivateKey(String.format("%04d", random.nextInt(10000)));
        med1.setPublicKey(String.format("%04d", random.nextInt(10000)));

        return med1;
    }
}
