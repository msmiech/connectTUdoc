package at.connectTUdoc.backend.dto;

import at.connectTUdoc.backend.dto.abstracts.AbstractDTOTest;
import at.connectTUdoc.backend.model.Speciality;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This test class contains the test cases for the specific DTO
 */
public class SpecialityDTOTest extends AbstractDTOTest {

    @Test
    public void convertToDto_Correct(){
        SpecialityDTO specialityDTO = modelMapper.map(TestUtilsMedConnect.getSingleSpeciality(), SpecialityDTO.class);
        assertEquals(specialityDTO, TestUtilsMedConnect.getSingleSpecialityDTO());
    }

    @Test
    public void convertToEntity_Correct() {
         assertEquals(modelMapper.map(TestUtilsMedConnect.getSingleSpecialityDTO(), Speciality.class), TestUtilsMedConnect.getSingleSpeciality());
    }
}
