package at.connectTUdoc.backend.dto;

import at.connectTUdoc.backend.dto.abstracts.AbstractDTOTest;
import at.connectTUdoc.backend.model.ChatThread;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This test class contains the test cases for the specific DTO
 */
public class ChatThreadDTOTest extends AbstractDTOTest {

    @Test
    public void convertEntityToDto_Correct() {
        assertEquals(modelMapper.map(TestUtilsMedConnect.getChatThread1(), ChatThreadDTO.class), TestUtilsMedConnect.getChatThread1DTO());
    }

    @Test
    public void convertDtoToEntity_Correct() {
        assertEquals( modelMapper.map(TestUtilsMedConnect.getChatThread1DTO(), ChatThread.class), TestUtilsMedConnect.getChatThread1());
    }

    @Test
    public void convertEntityToDto_False() {
        assertNotEquals(modelMapper.map(TestUtilsMedConnect.getChatThread1(), ChatThreadDTO.class), TestUtilsMedConnect.getChatThread2DTO());
    }

    @Test
    public void convertDtoToEntity_False() {
        assertNotEquals( modelMapper.map(TestUtilsMedConnect.getChatThread1DTO(), ChatThread.class), TestUtilsMedConnect.getChatThread2());
    }

}
