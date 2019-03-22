package at.connectTUdoc.backend.dto;

import at.connectTUdoc.backend.dto.abstracts.AbstractDTOTest;
import at.connectTUdoc.backend.model.Patient;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains the test cases for the specific DTO
 */
public class PatientDTOTest extends AbstractDTOTest {

    @Test
    public void convertToDto_Correct() {
        assertEquals(modelMapper.map(TestUtilsMedConnect.getPatient1(), PatientDTO.class), TestUtilsMedConnect.getPatient1DTO());
    }

    @Test
    public void convertToEntity_Correct() {
        assertEquals(modelMapper.map(TestUtilsMedConnect.getPatient1DTO(), Patient.class), TestUtilsMedConnect.getPatient1());
    }
}
