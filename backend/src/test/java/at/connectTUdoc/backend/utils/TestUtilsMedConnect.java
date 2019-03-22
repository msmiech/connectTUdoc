package at.connectTUdoc.backend.utils;

import at.connectTUdoc.backend.dto.*;
import at.connectTUdoc.backend.model.*;
import at.docTUconnectr.backend.dto.*;
import at.docTUconnectr.backend.model.*;
import at.medconnect.backend.dto.*;
import at.medconnect.backend.model.*;
import at.ws18_ase_qse_03.backend.dto.*;
import at.ws18_ase_qse_03.backend.model.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This helper class contains the objects used for testing purposes
 */
public final class TestUtilsMedConnect {

    private static final ModelMapper modelMapper = new ModelMapper();

    private static void setDatabaseReferenceIdsToGeneratedObject(MedicalWorker databaseObj, MedicalWorker generatedObj) {
        generatedObj.setId(databaseObj.getId());
        if (databaseObj.getSpecialities() != null && generatedObj.getSpecialities() != null) {
            for (int i = 0; i < databaseObj.getSpecialities().size(); i++) {
                generatedObj.getSpecialities().get(i).setId(databaseObj.getSpecialities().get(i).getId());
            }
        }
    }

    private static void setDatabaseReferenceIdsToGeneratedObject(Office databaseObj, Office generatedObj) {
        generatedObj.setId(databaseObj.getId());
        if (databaseObj.getAddress() != null && generatedObj.getAddress() != null) {
            generatedObj.getAddress().setId(databaseObj.getAddress().getId());
        }
        if (databaseObj.getAppointments() != null && generatedObj.getAppointments() != null) {
            for (int i = 0; i < databaseObj.getAppointments().size(); i++) {
                generatedObj.getAppointments().get(i).setId(databaseObj.getAppointments().get(i).getId());
                generatedObj.getAppointments().get(i).setOffice(databaseObj.getAppointments().get(i).getOffice());
            }
        }
        if (databaseObj.getOfficehours() != null && generatedObj.getOfficehours() != null) {
            for (int i = 0; i < databaseObj.getOfficehours().size(); i++) {
                generatedObj.getOfficehours().get(i).setId(databaseObj.getOfficehours().get(i).getId());
            }
        }
        if (databaseObj.getOfficeWorkers() != null && generatedObj.getOfficeWorkers() != null) {
            for (int i = 0; i < databaseObj.getOfficeWorkers().size(); i++) {
                setDatabaseReferenceIdsToGeneratedObject(databaseObj.getOfficeWorkers().get(i), generatedObj.getOfficeWorkers().get(i));
            }
        }
    }

    public static void setDatabaseReferenceIdToGeneratedListObjects(List<Office> databaseObj, List<Office> generatedObj) {
        for (int i = 0; i < databaseObj.size(); i++) {
            setDatabaseReferenceIdsToGeneratedObject(databaseObj.get(i), generatedObj.get(i));
        }

    }

    /**
     * @return a single medicinedto object
     */
    public static MedicineDTO getSingleMedicineDTO() {
        MedicineDTO dto = new MedicineDTO();
        dto.setId("1329497");
        dto.setName("Seractil forte 400mg-Filmtabletten");
        return dto;
    }

    /**
     * @return a single medicine object
     */
    public static Medicine getSingleMedicine() {
        Medicine med = new Medicine();
        med.setId("1329497");
        med.setName("Seractil forte 400mg-Filmtabletten");
        return med;
    }


    /**
     * @return a converted single medical worker object to DTO with the model mapper
     */
    public static MedicalWorkerDTO getSingleMedicalWorkerDTO() {
        MedicalWorkerDTO med1 = new MedicalWorkerDTO();

        med1.setId(7L);
        med1.seteMail("test@test.at");
        med1.setType(MedWorkerType.DOCTOR);
        med1.setFirstName("Marcus");
        med1.setLastName("Dracula");
        med1.setPosTitle("ire");
        med1.setPreTitle("Vamp");
        med1.setPrivateKey("1234");
        med1.setPublicKey("5678");
        med1.setSpecialities(List.of(getSingleSpecialityDTO()));
        return med1;
    }

    /**
     * @return a converted single medical worker object to DTO with the model mapper
     */
    public static MedicalWorkerDTO getSingleMedicalWorkerDTO_ID100() {
        MedicalWorkerDTO med1 = new MedicalWorkerDTO();

        med1.setId(1000L);
        med1.seteMail("test@test.at");
        med1.setType(MedWorkerType.DOCTOR);
        med1.setFirstName("Marcus");
        med1.setLastName("Dracula");
        med1.setPosTitle("ire");
        med1.setPreTitle("Vamp");
        med1.setPrivateKey("1234");
        med1.setPublicKey("5678");
        med1.setSpecialities(List.of(getSingleSpecialityDTO()));
        return med1;
    }

    /**
     * @return a converted single address object to DTO with the model mapper
     */
    public static AddressDTO getSingleAddressDTO() {
        AddressDTO dto = new AddressDTO();
        dto.setId(5L);
        dto.setCity("Test 1");
        dto.setCountry("Austria");
        dto.setDoor("1");
        dto.setFloor("7");
        dto.setNumber("12");
        dto.setPlace("Vienna");
        dto.setStreet("Wimbledon");
        dto.setZip(1235);
        return dto;
    }

    /**
     * @return a converted single appointment object to DTO with the model mapper
     */
    public static AppointmentDTO getSingleAppointmentDTO() {
        AppointmentDTO dto = new AppointmentDTO();

        dto.setId(6L);
        dto.setAppointmentBegin(LocalDateTime.of(2017, 2, 11, 11, 11));
        dto.setAppointmentEnd(LocalDateTime.of(2017, 2, 11, 12, 11));

        OfficeDTO officedto = new OfficeDTO();

        officedto.setId(5L);
        officedto.setAddress(getSingleAddressDTO());
        officedto.setEmail("office@hugo.at");
        officedto.setFax("02658 741258");
        officedto.setName("Arztpraxis");
        officedto.setPhone("125478");
        officedto.setOfficehours(getOfficeHourDTOs());
        officedto.setOfficeWorkers(getMedicalWorkerDTOs(3));

        dto.setOffice(officedto);

        return dto;
    }

    /**
     * @return a converted single speciality object to DTO with the model mapper
     */
    public static SpecialityDTO getSingleSpecialityDTO() {
        SpecialityDTO spHuman = new SpecialityDTO();
        spHuman.setId(7L);
        spHuman.setSpecialityName("Human");
        return spHuman;
    }

    /**
     * @return a converted single office object to DTO with the model mapper
     */
    public static OfficeHourDTO getSingleOfficeHourDTO() {
        OfficeHourDTO dto = new OfficeHourDTO();
        dto.setId(19L);
        dto.setDaytype(DayType.MONTAG);
        dto.setBeginTime(LocalTime.of(7, 0, 0));
        dto.setEndTime(LocalTime.of(12, 0, 0));
        return dto;
    }

    /**
     * returns a single office hour with fixed times and day.
     * Times: begin: 07:00 | end 12:00
     * Day: DayType-Monday
     *
     * @return s single office hour with fixed values
     */
    public static OfficeHour getSingleOfficeHour() {
        return getSingleOfficeHour("07:00", "12:00", DayType.MONTAG);
    }

    /**
     * @return a converted single office object to DTO with the model mapper
     */
    public static OfficeDTO getSingleOfficeDTO() {
        OfficeDTO dto = new OfficeDTO();

        dto.setId(5L);
        dto.setAddress(getSingleAddressDTO());
        dto.setAppointments(List.of(getSingleAppointmentDTO()));
        dto.getAppointments().forEach(appointment -> appointment.setOffice(dto));
        dto.setEmail("office@hugo.at");
        dto.setFax("02658 741258");
        dto.setName("Arztpraxis");
        dto.setPhone("125478");
        dto.setOfficehours(getOfficeHourDTOs());
        dto.setOfficeWorkers(getMedicalWorkerDTOs(3));


        return dto;
    }

    public static OfficeDTO getSingleOfficeDTOWithoutAppointments() {
        OfficeDTO dto = new OfficeDTO();

        dto.setId(5L);
        dto.setAddress(getSingleAddressDTO());
        dto.setEmail("office@hugo.at");
        dto.setFax("02658 741258");
        dto.setName("Arztpraxis");
        dto.setPhone("125478");
        dto.setOfficehours(getOfficeHourDTOs());
        dto.setOfficeWorkers(getMedicalWorkerDTOs(3));


        return dto;
    }

    /**
     * @return a single medical worker object with fixed values
     */
    public static MedicalWorker getSingleMedicalWorker() {
        MedicalWorker med1 = new MedicalWorker();

        med1.setId(7L);
        med1.seteMail("test@test.at");
        med1.setType(MedWorkerType.DOCTOR);
        med1.setSpecialities(getSpecialities());
        med1.setFirstName("Marcus");
        med1.setLastName("Dracula");
        med1.setPosTitle("ire");
        med1.setPreTitle("Vamp");
        med1.setPrivateKey("1234");
        med1.setPublicKey("5678");

        return med1;
    }

    /**
     * @return a list of size 1 with fixed value
     */
    public static List<ChatThread> getChatThreads(int number) {
        List<ChatThread> chatThreads = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            chatThreads.add(getSingleChatThread());
        }
        return chatThreads;
    }

    /**
     * @return a list of size 1 with fixed value
     */
    private static ChatThread getSingleChatThread() {
        ChatThread chatThread = new ChatThread();
        chatThread.setId(1L);
        return chatThread;
    }

    /**
     * @return a list of size 1 with fixed value
     */
    private static List<Speciality> getSpecialities() {
        List<Speciality> specialities = new ArrayList<>();
        specialities.add(getSingleSpeciality());
        return specialities;
    }

    private static List<Speciality> getSpecialities(long i) {
        List<Speciality> specialities = new ArrayList<>();
        Speciality speciality = getSingleSpeciality();
        speciality.setId(7 + i);
        specialities.add(speciality);
        return specialities;
    }


    /**
     * @return a list of size 1 with fixed value
     */
    private static List<SpecialityDTO> getSpecialitieDTOs() {
        List<SpecialityDTO> specialities = new ArrayList<>();
        specialities.add(getSingleSpecialityDTO());
        return specialities;
    }

    private static List<SpecialityDTO> getSpecialitieDTOs(long i) {
        List<SpecialityDTO> specialities = new ArrayList<>();
        SpecialityDTO speciality = getSingleSpecialityDTO();
        speciality.setId(7 + i);
        specialities.add(speciality);
        return specialities;
    }

    /**
     * @return a single speciality with name Human
     */
    public static Speciality getSingleSpeciality() {
        // new one
        Speciality spHuman = new Speciality();
        spHuman.setId(7L);
        spHuman.setSpecialityName("Human");
        return spHuman;
    }

    /**
     * @param number - the number of cloned medical worker objects
     * @return a list of medical worker objects at size of the param number
     */
    public static List<MedicalWorker> getMedicalWorkers(int number) {
        List<MedicalWorker> workerList = new ArrayList<>();
        for (long i = 0, id = 7; i < number; i++, id++) {
            MedicalWorker med1 = new MedicalWorker();
            med1.setId(id);
            med1.setUid("uid" + id);
            med1.seteMail("test@test.at");
            med1.setType(MedWorkerType.DOCTOR);
            med1.setSpecialities(getSpecialities(i));
            med1.setFirstName("Marcus");
            med1.setLastName("Dracula");
            med1.setPosTitle("ire");
            med1.setPreTitle("Vamp");
            med1.setPrivateKey("1234");
            med1.setPublicKey("5678");

            workerList.add(med1);
        }

        return workerList;

    }

    /**
     * @param number - the number of cloned medical worker objects
     * @return a list of medical worker objects at size of the param number
     */
    public static List<MedicalWorkerDTO> getMedicalWorkerDTOs(int number) {
        List<MedicalWorkerDTO> workerList = new ArrayList<>();
        for (long i = 0, id = 7; i < number; i++, id++) {
            MedicalWorkerDTO med1 = new MedicalWorkerDTO();
            med1.setId(id);
            med1.setUid("uid" + id);
            med1.seteMail("test@test.at");
            med1.setType(MedWorkerType.DOCTOR);
            med1.setSpecialities(getSpecialitieDTOs(i));
            med1.setFirstName("Marcus");
            med1.setLastName("Dracula");
            med1.setPosTitle("ire");
            med1.setPreTitle("Vamp");
            med1.setPrivateKey("1234");
            med1.setPublicKey("5678");

            workerList.add(med1);
        }

        return workerList;

    }

    /**
     * @return a single office instance with fixed values
     */
    public static Office getSingleOffice() {
        Office office = new Office();

        office.setId(5L);
        office.setAddress(getSingleAddress());
        office.setAppointments(getAppointments());
        office.getAppointments().forEach(appointment -> appointment.setOffice(office));
        office.setEmail("office@hugo.at");
        office.setFax("02658 741258");
        office.setName("Arztpraxis");
        office.setPhone("125478");
        office.setOfficeHours(getOfficeHours());
        office.setOfficeWorkers(getMedicalWorkers(3));

        return office;
    }

    /**
     * @param number - the number of medical workers assigned to the generated office
     * @return an office with a number of assigned medical workers
     */
    public static OfficeDTO getOfficeDTOWithNumberAssignedMedicalWorkersDTO(Integer number) {
        OfficeDTO office = getSingleOfficeDTO();
        List<MedicalWorkerDTO> medicalWorkers = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            medicalWorkers.add(getSingleMedicalWorkerDTO());
        }
        office.setOfficeWorkers(medicalWorkers);
        return office;
    }

    /**
     * @return a list of four officeHours objects with fixed values
     */
    private static List<OfficeHour> getOfficeHours() {
        List<OfficeHour> officeHours;
        officeHours = new ArrayList<>();
        officeHours.add(getSingleOfficeHour(19L, LocalTime.of(7, 0), LocalTime.of(12, 0), DayType.MONTAG));
        officeHours.add(getSingleOfficeHour(20L, LocalTime.of(13, 0), LocalTime.of(16, 0), DayType.MONTAG));
        officeHours.add(getSingleOfficeHour(21L, LocalTime.of(8, 0), LocalTime.of(11, 0), DayType.DIENSTAG));
        officeHours.add(getSingleOfficeHour(22L, LocalTime.of(13, 0), LocalTime.of(18, 0), DayType.DONNERSTAG));
        return officeHours;
    }

    /**
     * @return a list of four officeHours objects with fixed values
     */
    private static List<OfficeHourDTO> getOfficeHourDTOs() {
        List<OfficeHourDTO> officeHours;
        officeHours = new ArrayList<>();
        officeHours.add(getSingleOfficeHourDTO(19L, LocalTime.of(7, 0), LocalTime.of(12, 0), DayType.MONTAG));
        officeHours.add(getSingleOfficeHourDTO(20L, LocalTime.of(13, 0), LocalTime.of(16, 0), DayType.MONTAG));
        officeHours.add(getSingleOfficeHourDTO(21L, LocalTime.of(8, 0), LocalTime.of(11, 0), DayType.DIENSTAG));
        officeHours.add(getSingleOfficeHourDTO(22L, LocalTime.of(13, 0), LocalTime.of(18, 0), DayType.DONNERSTAG));
        return officeHours;
    }

    /**
     * @return a single appointment object with fixed values
     */
    public static Appointment getSingleAppointment() {
        Appointment appointment;
        appointment = new Appointment();

        appointment.setId(6L);
        appointment.setAppointmentBegin(LocalDateTime.of(2017, 2, 11, 11, 11));
        appointment.setAppointmentEnd(LocalDateTime.of(2017, 2, 11, 12, 11));

        Office office = new Office();

        office.setId(5L);
        office.setAddress(getSingleAddress());
        office.setEmail("office@hugo.at");
        office.setFax("02658 741258");
        office.setName("Arztpraxis");
        office.setPhone("125478");
        office.setOfficeHours(getOfficeHours());
        office.setOfficeWorkers(getMedicalWorkers(3));
        appointment.setOffice(office);

        return appointment;
    }

    /**
     * @return a list of appointment objects of size 1
     */
    private static List<Appointment> getAppointments() {
        List<Appointment> appointments;
        appointments = new ArrayList<>();

        Appointment appointment = new Appointment();
        appointment.setId(6L);
        appointment.setAppointmentBegin(LocalDateTime.of(2017, 2, 11, 11, 11));
        appointment.setAppointmentEnd(LocalDateTime.of(2017, 2, 11, 12, 11));
        appointments.add(appointment);
        return appointments;
    }

    /**
     * @return a single address object with fixed values
     */
    public static Address getSingleAddress() {
        Address address;
        address = new Address();
        address.setId(5L);
        address.setCity("Test 1");
        address.setCountry("Austria");
        address.setDoor("1");
        address.setFloor("7");
        address.setNumber("12");
        address.setPlace("Vienna");
        address.setStreet("Wimbledon");
        address.setZip(1235);
        return address;
    }

    /**
     * @param number - the number of cloned office objects
     * @return a list of office objects at size of the param number
     */
    public static List<Office> getOffices(int number) {
        List<Office> officeList = new ArrayList<>();
        for (long i = 0, id = 5; i < number; i++, id++) {
            Office office = new Office();
            office.setId(id);
            office.setAddress(getSingleAddress());
            office.setAppointments(getAppointments());
            office.getAppointments().forEach(appointment -> appointment.setOffice(office));
            office.setEmail("office@hugo.at");
            office.setFax("02658 741258");
            office.setName("Arztpraxis");
            office.setPhone("125478");
            office.setOfficeHours(getOfficeHours());
            office.setOfficeWorkers(getMedicalWorkers(3));
            officeList.add(office);
        }

        return officeList;
    }

    /**
     * @param beginTime - the begin time of the office hour
     * @param endTime   - the end time of the office hour
     * @param dayType   - the day where the office hour is set
     * @return a single office hour object with the param values
     */
    public static OfficeHour getSingleOfficeHour(String beginTime, String endTime, DayType dayType) {
        OfficeHour officeHour = new OfficeHour();
        officeHour.setId(19L);
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime begiLocalTimen = LocalTime.parse(beginTime, dtfTime);
        LocalTime endLocalTime = LocalTime.parse(endTime, dtfTime);
        officeHour.setBeginTime(begiLocalTimen);
        officeHour.setEndTime(endLocalTime);
        officeHour.setDaytype(dayType);
        return officeHour;
    }

    /**
     * @param id        - id of the officehour
     * @param beginTime - the begin time of the office hour
     * @param endTime   - the end time of the office hour
     * @param dayType   - the day where the office hour is set
     * @return a single office hour object with the param values
     */
    private static OfficeHour getSingleOfficeHour(long id, LocalTime beginTime, LocalTime endTime, DayType dayType) {
        OfficeHour officeHour = new OfficeHour();
        officeHour.setId(id);
        officeHour.setBeginTime(beginTime);
        officeHour.setEndTime(endTime);
        officeHour.setDaytype(dayType);
        return officeHour;
    }

    /**
     * @param id        - id of the officehourdto
     * @param beginTime - the begin time of the office hour dto
     * @param endTime   - the end time of the office hour dto
     * @param dayType   - the day where the office hour dto is set
     * @return a single office hour object dto with the param values
     */
    private static OfficeHourDTO getSingleOfficeHourDTO(long id, LocalTime beginTime, LocalTime endTime, DayType dayType) {
        OfficeHourDTO officeHour = new OfficeHourDTO();
        officeHour.setId(id);
        officeHour.setBeginTime(beginTime);
        officeHour.setEndTime(endTime);
        officeHour.setDaytype(dayType);
        return officeHour;
    }

    public static Patient getPatient1() {
        Patient patient = new Patient();
        patient.setUid("987654321");
        patient.seteMail("andreas@huber.at");
        patient.setFirstName("Andread");
        patient.setLastName("Huber");
        patient.setPosTitle("");
        patient.setPreTitle("Dr.");
        patient.setPrivateKey("1234");
        patient.setPublicKey("5678");
        patient.setSvnr("1234567890");

        return patient;
    }

    public static PatientDTO getPatient1DTO() {
        PatientDTO patient = new PatientDTO();
        patient.setUid("987654321");
        patient.seteMail("andreas@huber.at");
        patient.setFirstName("Andread");
        patient.setLastName("Huber");
        patient.setPosTitle("");
        patient.setPreTitle("Dr.");
        patient.setPrivateKey("1234");
        patient.setPublicKey("5678");
        patient.setSvnr("1234567890");

        return patient;
    }

    public static Patient getPatient2() {
        Patient patient = new Patient();
        patient.setUid("123456789");
        patient.seteMail("manni@mueller.at");
        patient.setFirstName("Manni");
        patient.setLastName("Mueller");
        patient.setPosTitle("Bsc.");
        patient.setPreTitle("");
        patient.setPrivateKey("1234");
        patient.setPublicKey("5678");
        patient.setSvnr("0987654321");

        return patient;
    }

    public static PatientDTO getPatient2DTO() {
        PatientDTO patient = new PatientDTO();
        patient.setUid("123456789");
        patient.seteMail("manni@mueller.at");
        patient.setFirstName("Manni");
        patient.setLastName("Mueller");
        patient.setPosTitle("Bsc.");
        patient.setPreTitle("");
        patient.setPrivateKey("1234");
        patient.setPublicKey("5678");
        patient.setSvnr("0987654321");

        return patient;
    }

    public static ChatThread getChatThread1() {
        ChatThread chatThread = new ChatThread();
        chatThread.setId(1L);
        chatThread.setOffice(getSingleOffice());
        chatThread.setPatient(getPatient1());

        return chatThread;
    }

    public static ChatThreadDTO getChatThread1DTO() {
        ChatThreadDTO chatThread = new ChatThreadDTO();
        chatThread.setId(1L);
        chatThread.setOffice(getSingleOfficeDTO());
        chatThread.setPatient(getPatient1DTO());

        return chatThread;
    }

    public static ChatThread getChatThread2() {
        ChatThread chatThread = new ChatThread();
        chatThread.setId(2L);
        chatThread.setOffice(getSingleOffice());
        chatThread.setPatient(getPatient2());

        return chatThread;
    }

    public static ChatThreadDTO getChatThread2DTO() {
        ChatThreadDTO chatThread = new ChatThreadDTO();
        chatThread.setId(2L);
        chatThread.setOffice(getSingleOfficeDTO());
        chatThread.setPatient(getPatient2DTO());

        return chatThread;
    }

    public static ChatMessage getMessageEarlier() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(3L);
        chatMessage.setChatThread(getChatThread1());
        chatMessage.setCreateDateTime(LocalDateTime.of(2018, 12, 9, 22, 0));
        chatMessage.setMessage("Test patient message 1");
        chatMessage.setChatAttachment("");
        chatMessage.setPatientMessage(true);
        chatMessage.setChatAttachmentPresent(true);

        return chatMessage;
    }

    public static ChatMessage getMessageLater() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(1L);
        chatMessage.setChatThread(getChatThread1());
        chatMessage.setCreateDateTime(LocalDateTime.of(2018, 12, 9, 23, 0));
        chatMessage.setMessage("Test office message 1");
        chatMessage.setChatAttachment("");
        chatMessage.setPatientMessage(false);
        chatMessage.setChatAttachmentPresent(true);

        return chatMessage;
    }

    public static ChatMessage getOtherMessage() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(2L);
        chatMessage.setChatThread(getChatThread2());
        chatMessage.setCreateDateTime(LocalDateTime.of(2018, 12, 9, 22, 1));
        chatMessage.setMessage("Test office message 1");
        chatMessage.setChatAttachment("");
        chatMessage.setPatientMessage(false);
        chatMessage.setChatAttachmentPresent(true);

        return chatMessage;
    }

    public static ChatMessageDTO getMessageEarlierDTO() {
        ChatMessageDTO chatMessage = new ChatMessageDTO();
        chatMessage.setId(3L);
        chatMessage.setChatThread(getChatThread1DTO());
        chatMessage.setCreateDateTime(LocalDateTime.of(2018, 12, 9, 22, 0));
        chatMessage.setMessage("Test patient message 1");
        ChatAttachmentDTO chatAttachment = new ChatAttachmentDTO();
        chatMessage.setChatAttachment(chatAttachment);
        chatMessage.setPatientMessage(true);
        chatMessage.setChatAttachmentPresent(true);

        return chatMessage;
    }

    public static ChatMessageDTO getMessageLaterDTO() {
        ChatMessageDTO chatMessage = new ChatMessageDTO();
        chatMessage.setId(1L);
        chatMessage.setChatThread(getChatThread1DTO());
        chatMessage.setCreateDateTime(LocalDateTime.of(2018, 12, 9, 23, 0));
        chatMessage.setMessage("Test office message 1");
        ChatAttachmentDTO chatAttachment = new ChatAttachmentDTO();
        chatMessage.setChatAttachment(chatAttachment);
        chatMessage.setPatientMessage(false);
        chatMessage.setChatAttachmentPresent(true);

        return chatMessage;
    }

    public static ChatMessageDTO getOtherMessageDTO() {
        ChatMessageDTO chatMessage = new ChatMessageDTO();
        chatMessage.setId(2L);
        chatMessage.setChatThread(getChatThread2DTO());
        chatMessage.setCreateDateTime(LocalDateTime.of(2018, 12, 9, 22, 1));
        chatMessage.setMessage("Test office message 1");
        ChatAttachmentDTO chatAttachment = new ChatAttachmentDTO();
        chatMessage.setChatAttachment(chatAttachment);
        chatMessage.setPatientMessage(false);
        chatMessage.setChatAttachmentPresent(true);

        return chatMessage;
    }
}
