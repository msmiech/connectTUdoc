package at.connectTUdoc.backend.service;

import at.connectTUdoc.backend.dao.MedicineRepository;
import at.connectTUdoc.backend.dto.MedicineDTO;
import at.connectTUdoc.backend.service.impl.MedicineServiceImpl;
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

import java.util.Optional;

/**
 * Service Tests for CRUD operations are no benefit, cause there is no ongoing manipulation threw the service-layer.
 * For example some methods are shown to show how to mock these methods.
 */
@RunWith(SpringRunner.class)
public class MedicineServiceTest {
    @TestConfiguration
    static class MedicineServiceTestContextConfiguration {

        @Bean
        public MedicineService medicineService() {
            return new MedicineServiceImpl();
        }

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }

    }

    @Autowired
    MedicineService medicineService;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    private MedicineRepository medicineRepository;

    @Test
    public void getMedicineById_shouldReturnMedicine() {
        Mockito.when(medicineRepository.findById(TestUtilsMedConnect.getSingleMedicine().getId())).thenReturn(Optional.of(TestUtilsMedConnect.getSingleMedicine()));

        MedicineDTO medicineDTO = medicineService.getMedicineById(TestUtilsMedConnect.getSingleMedicine().getId());

        Assert.assertEquals(TestUtilsMedConnect.getSingleMedicineDTO().getId(), medicineDTO.getId());
        Assert.assertEquals(TestUtilsMedConnect.getSingleMedicineDTO().getName(), medicineDTO.getName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findPatientByUid_shoulThrowResourceNotFoundExceptionIfNoPatientFound() {
        Mockito.when(medicineRepository.findById(TestUtilsMedConnect.getSingleMedicine().getId() + "4")).thenReturn(Optional.empty());

        medicineService.getMedicineById(TestUtilsMedConnect.getSingleMedicine().getId() + "4");
    }

}
