package at.connectTUdoc.backend.service.impl;

import at.connectTUdoc.backend.controller.PatientController;
import at.connectTUdoc.backend.dao.PatientRepository;
import at.connectTUdoc.backend.dto.PatientDTO;
import at.connectTUdoc.backend.model.Patient;
import at.connectTUdoc.backend.service.FirebaseService;
import at.connectTUdoc.backend.service.PatientService;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.UserRecord;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    FirebaseService firebaseService;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Boolean existsByUid(String uid) {
        return patientRepository.existsByUid(uid);
    }

    @Override
    public PatientDTO findPatientByUid(String uid) {
        Patient patient = patientRepository.findPatientByUid(uid);
        if(patient == null){
            throw new ResourceNotFoundException("Kein Patient mit der ID: " + uid + " gespeichert.");
        } else {
            return modelMapper.map(patientRepository.findPatientByUid(uid), PatientDTO.class);
        }
    }

    @Override
    public PatientDTO findPatientById(Long id) {
        return modelMapper.map(patientRepository.findById(id), PatientDTO.class);
    }

    @Override
    public PatientDTO createOrUpdatePatient(PatientDTO patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient muss existieren!");
        }
        return modelMapper.map(patientRepository.save(modelMapper.map(patient, Patient.class)), PatientDTO.class);
    }

    @Override
    public PatientDTO retrieveCurrentPatient(String uid) throws FirebaseException {
        if (this.existsByUid(uid)) {
            LOGGER.info("User does already exist in MedConnect DB!");
            return this.findPatientByUid(uid);
        } // else not needed after return

        LOGGER.info("User doesn't exist in MedConnect DB!");

        UserRecord user = firebaseService.getUserByUid(uid);
        PatientDTO newPatient = new PatientDTO();
        newPatient.setUid(uid);
        newPatient.seteMail(user.getEmail());
        newPatient.setSvnr("");
        newPatient.setPreTitle("");
        newPatient.setPosTitle("");
        String[] splittedName = user.getDisplayName().split(" ", 2);
        if (splittedName.length == 2) {
            newPatient.setFirstName(splittedName[0]);
            newPatient.setLastName(splittedName[1]);
        } else if (splittedName.length == 1) {
            newPatient.setFirstName(splittedName[0]);
        }

        LOGGER.info("New user: " + newPatient.geteMail());
        return this.createOrUpdatePatient(newPatient);
    }

    @Override
    public void deletePatientByUid(String uid) {
        patientRepository.deletePatientByUid(uid);
    }
}
