package at.connectTUdoc.backend.dto;

import at.connectTUdoc.backend.dto.abstracts.AbstractDTOTest;
import at.connectTUdoc.backend.model.ChatMessage;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This test class contains the test cases for the specific DTO
 */
@RunWith(SpringRunner.class)
public class ChatMessageDTOTest extends AbstractDTOTest {

    @Test
    public void convertEntityToDto_Correct() {
        assertEquals(modelMapper.map(TestUtilsMedConnect.getMessageEarlier(), ChatMessageDTO.class), TestUtilsMedConnect.getMessageEarlierDTO());
    }

    @Test
    public void convertDtoToEntity_Correct() {
        assertEquals( modelMapper.map(TestUtilsMedConnect.getMessageEarlierDTO(), ChatMessage.class), TestUtilsMedConnect.getMessageEarlier());
    }

    @Test
    public void convertEntityToDto_False() {
        assertNotEquals(modelMapper.map(TestUtilsMedConnect.getMessageEarlier(), ChatMessageDTO.class), TestUtilsMedConnect.getMessageLaterDTO());
    }

    @Test
    public void convertDtoToEntity_False() {
        assertNotEquals( modelMapper.map(TestUtilsMedConnect.getMessageEarlierDTO(), ChatMessage.class), TestUtilsMedConnect.getMessageLater());
    }

}
