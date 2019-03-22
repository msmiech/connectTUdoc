package at.connectTUdoc.backend.service.impl;

import at.connectTUdoc.backend.exception.UserNotAuthenticatedException;
import at.connectTUdoc.backend.dao.MedicalWorkerRepository;
import at.connectTUdoc.backend.dto.MedicalWorkerDTO;
import at.connectTUdoc.backend.model.MedicalWorker;
import at.connectTUdoc.backend.service.MedicalWorkerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalWorkerServiceImpl implements MedicalWorkerService {

    @Autowired
    MedicalWorkerRepository medicalWorkerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public MedicalWorkerDTO findMedicalWorkerById(long id) throws ResourceNotFoundException {
        Optional<MedicalWorker> result =  medicalWorkerRepository.findById(id);
        if(result.isPresent()){
            return modelMapper.map(result.get(),MedicalWorkerDTO.class);
        }
        throw new ResourceNotFoundException("Kein medical worker gefunden mit der id: " + id);
    }

    @Override
    public MedicalWorkerDTO createMedicalWorker(MedicalWorkerDTO medicalWorker) {
        return modelMapper.map(medicalWorkerRepository.save( modelMapper.map(medicalWorker,MedicalWorker.class)),MedicalWorkerDTO.class);
    }

    @Override
    public void deleteMedicalWorker(Long id) {
        medicalWorkerRepository.deleteById(id);
    }

    @Override
    public MedicalWorkerDTO updateMedicalWorker(MedicalWorkerDTO medicalWorker) {
        MedicalWorkerDTO medUpdate = new MedicalWorkerDTO();
        medUpdate.setPublicKey(medicalWorker.getPublicKey());
        medUpdate.setPrivateKey(medicalWorker.getPrivateKey());
        medUpdate.setPreTitle(medicalWorker.getPreTitle());
        medUpdate.setPosTitle(medicalWorker.getPosTitle());
        medUpdate.setLastName(medicalWorker.getLastName());
        medUpdate.setFirstName(medicalWorker.getFirstName());
        medUpdate.setSpecialities(medicalWorker.getSpecialities());
        medUpdate.setType(medicalWorker.getType());
        medUpdate.seteMail(medicalWorker.geteMail());

        return modelMapper.map(medicalWorkerRepository.save(modelMapper.map(medUpdate,MedicalWorker.class)),MedicalWorkerDTO.class);
    }

    @Override
    public List<MedicalWorkerDTO> findAllMedicalWorkers() throws ResourceNotFoundException{

        return Arrays.asList(modelMapper.map(medicalWorkerRepository.findAll(),MedicalWorkerDTO[].class));
    }

    @Override
    public MedicalWorkerDTO findMedicalWorkerByUID(String uid)
    {
        MedicalWorker medicalWorker = medicalWorkerRepository.findMedicalWorkerByUidEquals(uid);
        if(medicalWorker == null)
            throw new UserNotAuthenticatedException("Kein Benutzer verzeichnet mit der uid " + uid + " !");
        return modelMapper.map(medicalWorker, MedicalWorkerDTO.class);
    }
}
