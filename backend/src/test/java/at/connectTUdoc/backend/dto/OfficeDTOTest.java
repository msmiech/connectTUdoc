package at.connectTUdoc.backend.dto;

import at.connectTUdoc.backend.dto.abstracts.AbstractDTOTest;
import at.connectTUdoc.backend.model.Office;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains the test cases for the specific DTO
 */
@RunWith(SpringRunner.class)
public class OfficeDTOTest extends AbstractDTOTest {

    @Test
    public void convertToDto_Correct() {
        OfficeDTO officeDTO = modelMapper.map(TestUtilsMedConnect.getSingleOffice(), OfficeDTO.class);
        assertEquals(officeDTO, TestUtilsMedConnect.getSingleOfficeDTO());
    }

    @Test
    public void convertToEntity_Correct() {
        assertEquals(modelMapper.map(TestUtilsMedConnect.getSingleOfficeDTO(), Office.class), TestUtilsMedConnect.getSingleOffice());
    }
}
