package at.connectTUdoc.backend.dto;

import at.connectTUdoc.backend.dto.abstracts.AbstractDTOTest;
import at.connectTUdoc.backend.model.Medicine;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains the test cases for the specific DTO
 */
public class MedicineDTOTest extends AbstractDTOTest {

    @Test
    public void convertEntityToDto_Correct() {
        assertEquals(modelMapper.map(TestUtilsMedConnect.getSingleMedicine(), MedicineDTO.class), TestUtilsMedConnect.getSingleMedicineDTO());
    }

    @Test
    public void convertDtoToEntity_Correct() {
        assertEquals( modelMapper.map(TestUtilsMedConnect.getSingleMedicineDTO(), Medicine.class), TestUtilsMedConnect.getSingleMedicine());
    }
}
