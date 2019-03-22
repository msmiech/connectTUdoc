package at.connectTUdoc.backend.dto;

import at.connectTUdoc.backend.dto.abstracts.AbstractDTOTest;
import at.connectTUdoc.backend.model.OfficeHour;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains the test cases for the specific DTO
 */
public class OfficeHourDTOTest extends AbstractDTOTest {
    
    @Test
    public void convertToDto_Correct() {
        OfficeHourDTO officeHourDTO = modelMapper.map(TestUtilsMedConnect.getSingleOfficeHour(), OfficeHourDTO.class);
        assertEquals(officeHourDTO, TestUtilsMedConnect.getSingleOfficeHourDTO());
    }

    @Test
    public void convertToEntity_Correct() {
        assertEquals(modelMapper.map(TestUtilsMedConnect.getSingleOfficeHourDTO(), OfficeHour.class), TestUtilsMedConnect.getSingleOfficeHour());
    }

}
