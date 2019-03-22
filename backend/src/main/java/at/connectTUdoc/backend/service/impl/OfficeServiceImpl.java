package at.connectTUdoc.backend.service.impl;

import at.connectTUdoc.backend.dao.*;
import at.connectTUdoc.backend.dto.*;
import at.connectTUdoc.backend.exception.InvalidRegistrationCodeException;
import at.connectTUdoc.backend.model.*;
import at.docTUconnectr.backend.dao.*;
import at.docTUconnectr.backend.dto.*;
import at.docTUconnectr.backend.model.*;
import at.medconnect.backend.dao.*;
import at.medconnect.backend.dto.*;
import at.medconnect.backend.model.*;
import at.ws18_ase_qse_03.backend.dao.*;
import at.ws18_ase_qse_03.backend.dto.*;
import at.ws18_ase_qse_03.backend.model.*;
import at.connectTUdoc.backend.service.OfficeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class OfficeServiceImpl implements OfficeService {
    @Autowired
    MedicalWorkerRepository medicalWorkerRepository;
    @Autowired
    OfficeRepository officeRepository;
    @Autowired
    SpecialityRepository specialityRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    ChatThreadRepository chatThreadRepository;
    private ConcurrentHashMap<Long, RegistrationCodeDTO> patientRegistration = new ConcurrentHashMap<>();

    public OfficeServiceImpl() {


    }

    public OfficeServiceImpl(ConcurrentHashMap<Long,RegistrationCodeDTO> registration) {
        this.patientRegistration = registration;
    }



    @Override
    public List<OfficeDTO> findAllOffices() {

        return Arrays.asList(modelMapper.map(officeRepository.findAll(),OfficeDTO[].class));
    }

    @Override
    public List<OfficeDTO> findOfficesByName(String name) {

        return Arrays.asList(modelMapper.map(officeRepository.findOfficesByName(name),OfficeDTO[].class));
    }

    @Override
    public OfficeDTO findOfficeById(long id) throws ResourceNotFoundException {
        Optional<Office> res = officeRepository.findById(id);
        if(res.isPresent()){
            return modelMapper.map(res.get(),OfficeDTO.class);
        }
        throw new ResourceNotFoundException("Es wurde keine Praxis mit der id: " + id + " gefunden.");
    }

    @Override
    public OfficeDTO createOffice(OfficeDTO office) {
        return modelMapper.map(officeRepository.save(modelMapper.map(office,Office.class)),OfficeDTO.class);
    }

    @Override
    public void deleteOfficeById(long id) {
        officeRepository.deleteById(id);
    }

    @Override
    public OfficeDTO updateOffice(OfficeDTO office) {
        List<Appointment> updateAppointment = appointmentRepository.findAppointmentsByOffice(modelMapper.map(office, Office.class));
        office.setAppointments(Arrays.asList(modelMapper.map(updateAppointment, AppointmentDTO[].class)));
        return modelMapper.map(officeRepository.save(modelMapper.map(office,Office.class)),OfficeDTO.class);
    }

    @Override
    public void deleteAllOffices() {
        officeRepository.deleteAll();
    }

    @Override
    public OfficeDTO findOfficeByMedicalWorker(MedicalWorkerDTO medicalWorker) throws ResourceNotFoundException{
        Optional<Office> res = officeRepository.findOfficeByOfficeWorkersIn(modelMapper.map(medicalWorker, MedicalWorker.class));
        if(res.isPresent()){
            return modelMapper.map(res.get(),OfficeDTO.class);
        }
        throw new ResourceNotFoundException("Es wurde kein Angestellter mit der uid uid: " + medicalWorker.getUid() + " gefunden.");
    }

    @Override
    public List<OfficeDTO> getOfficesByPatient(PatientDTO patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient muss existieren!");
        }

        List<Office> offices = officeRepository.findOfficesByOfficePatientsIn(modelMapper.map(patient, Patient.class));
        return Arrays.asList(modelMapper.map(offices, OfficeDTO[].class));
    }

    @Override
    public List<OfficeDTO> getOfficeBySearchTextSpecialityNameOnly(String searchText) {
        List<Speciality> specialities = specialityRepository.findSpecialitiesBySpecialityNameIgnoreCaseContaining(searchText);
        List<MedicalWorker> workers = medicalWorkerRepository.findMedicalWorkerBySpecialities(specialities);
        return Arrays.asList(modelMapper.map(officeRepository.findOfficesByOfficeWorkersIn(workers),OfficeDTO[].class));
    }

    @Override
    public List<OfficeDTO> getOfficeBySearchTextOfficeNameOnly(String searchText) {
        return Arrays.asList(modelMapper.map(officeRepository.findOfficesByNameIgnoreCaseContaining(searchText),OfficeDTO[].class));
    }

    @Override
    public List<OfficeDTO> getOfficeBySearchTextDoctorNameOnly(String searchText) {
        List<MedicalWorker> workers = medicalWorkerRepository.findMedicalWorkerByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(searchText, searchText);
        return Arrays.asList(modelMapper.map(officeRepository.findOfficesByOfficeWorkersIn(workers),OfficeDTO[].class));
    }

    @Override
    public List<PatientDTO> getPatientsByOfficeId(long id) {
        Office res = officeRepository.getOne(id);
        if(res == null){
            throw new ResourceNotFoundException("Keine Praxis mit der ID: " + id + " gespeichert.");
        }
        if(res.getOfficePatients() != null){
            return Arrays.asList(modelMapper.map(res.getOfficePatients(),PatientDTO[].class));
        }
        return new ArrayList<>();
    }

    @Override
    public List<PatientDTO> getUnconfirmedPatients(Long id) {
        Optional<Office> res = officeRepository.findById(id);
        if(!res.isPresent()){
            throw new ResourceNotFoundException("Keine Praxis mit der ID: " + id + " gespeichert.");
        }
        return Arrays.asList(modelMapper.map(patientRepository.getUnconfirmedPatients(res.get()),PatientDTO[].class));
    }

    @Override
    public void addPatientToOffice(Long officeId, Long patientId, RegistrationCodeDTO code) {
        Optional<Office> office = officeRepository.findById(officeId);
        if(!office.isPresent()){
            throw new ResourceNotFoundException("Keine Praxis mit der ID: " + officeId + " gespeichert.");
        }

        Optional<Patient> patient = patientRepository.findById(patientId);
        if(!patient.isPresent()){
            throw new ResourceNotFoundException("Kein Patient mit der ID: " + patientId + " gespeichert.");
        }
        try {
            validateRegistrationCode(patientId, code);
        } catch (InvalidRegistrationCodeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        Office updateOffice = office.get();
        updateOffice.addOfficePatient(patient.get());
        officeRepository.save(updateOffice);
        chatThreadRepository.save(new ChatThread(patient.get(),office.get()));
        this.patientRegistration.remove(patientId);

    }

    /**
     * The method validates the registration code of a patient. The code cannot be older
     * than 5 minutes and has to equal the code generated by the patient.
     * @param patientId id of the patient for which the code is verified
     * @param newCode code to be verified
     * @throws InvalidRegistrationCodeException if the given code is invalid
     */
    private void validateRegistrationCode(Long patientId, RegistrationCodeDTO newCode) throws InvalidRegistrationCodeException {
        RegistrationCodeDTO verifyCode = this.patientRegistration.get(patientId);
        if(verifyCode == null || newCode == null) {
            throw new ResourceNotFoundException("Registrierungscode konnte nicht gefunden werden. Bitte erzeugen Sie einen neuen Code.");
        }
        if(LocalDateTime.now().isAfter(verifyCode.getExpire())) {
            this.patientRegistration.remove(patientId);
            throw new InvalidRegistrationCodeException("Der Code ist bereits abgelaufen. Bitte erzeugen Sie einen neuen Code.");
        }
        if(!verifyCode.getCode().equals(newCode.getCode())) {
            throw new InvalidRegistrationCodeException("Der Code ist invalid. Bitte versuchen Sie es erneut.");
        }
    }

    @Override
    @Transactional
    public void deleteAppointmentsByOfficeANDPatient(Long officeId, Long patientId) {
        Optional<Office> office = officeRepository.findById(officeId);
        if(!office.isPresent()){
            throw new ResourceNotFoundException("Keine Praxis mit der ID: " + officeId + " gespeichert.");
        }

        Optional<Patient> patient = patientRepository.findById(patientId);
        if(!patient.isPresent()){
            throw new ResourceNotFoundException("Kein Patient mit der ID: " + patientId + " gespeichert.");
        }
        appointmentRepository.deleteAppointmentByOfficeAndPatient(office.get(),patient.get());
    }


    @Override
    public RegistrationCodeDTO getRegistrationCode(Long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if(!patient.isPresent()){
            throw new ResourceNotFoundException("Kein Patient mit der ID: " + patientId + " gespeichert.");
        }
        RegistrationCodeDTO code = new RegistrationCodeDTO();
        patientRegistration.put(patientId,code);
        return code;
    }
}
