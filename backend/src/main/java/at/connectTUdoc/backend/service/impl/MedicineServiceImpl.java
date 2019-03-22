package at.connectTUdoc.backend.service.impl;

import at.connectTUdoc.backend.dao.MedicineRepository;
import at.connectTUdoc.backend.dto.MedicineDTO;
import at.connectTUdoc.backend.model.Medicine;
import at.connectTUdoc.backend.service.MedicineService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MedicineServiceImpl implements MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    ModelMapper modelMapper;

    /**
     * @param id id of the medicine to search for
     * @return medicine with the given id
     */
    @Override
    public MedicineDTO getMedicineById(String id) {
        Optional<Medicine> medicine = medicineRepository.findById(id);
        if(!medicine.isPresent()) {
            throw new ResourceNotFoundException("Diese Medizin existiert nicht.");
        }
        return modelMapper.map(medicine.get(), MedicineDTO.class);
    }
}
