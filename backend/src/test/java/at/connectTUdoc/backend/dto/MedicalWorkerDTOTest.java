package at.connectTUdoc.backend.dto;

import at.connectTUdoc.backend.dto.abstracts.AbstractDTOTest;
import at.connectTUdoc.backend.model.MedicalWorker;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This test class contains the test cases for the specific DTO
 */
public class MedicalWorkerDTOTest extends AbstractDTOTest {

    @Test
    public void convertToDto_Correct() {
        assertEquals(modelMapper.map(TestUtilsMedConnect.getSingleMedicalWorker(), MedicalWorkerDTO.class), TestUtilsMedConnect.getSingleMedicalWorkerDTO());
    }

    @Test
    public void convertToEntity_Correct() {
        assertEquals(modelMapper.map(TestUtilsMedConnect.getSingleMedicalWorkerDTO(), MedicalWorker.class), TestUtilsMedConnect.getSingleMedicalWorker());
    }
}
