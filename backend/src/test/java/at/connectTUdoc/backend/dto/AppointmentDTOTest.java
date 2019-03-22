package at.connectTUdoc.backend.dto;

import at.connectTUdoc.backend.dto.abstracts.AbstractDTOTest;
import at.connectTUdoc.backend.model.Appointment;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains the test cases for the specific DTO
 */
public class AppointmentDTOTest extends AbstractDTOTest {

    @Test
    public void convertToDto_Correct() {
        Appointment appointment = TestUtilsMedConnect.getSingleAppointment();
        appointment.setOffice(TestUtilsMedConnect.getSingleOffice());

        AppointmentDTO dto = TestUtilsMedConnect.getSingleAppointmentDTO();
        dto.setOffice(TestUtilsMedConnect.getSingleOfficeDTO());


        assertEquals(modelMapper.map(appointment, AppointmentDTO.class), dto);
    }

    @Test
    public void convertDtoToEntity_Correct() {
        assertEquals(modelMapper.map(TestUtilsMedConnect.getSingleAppointmentDTO(), Appointment.class), TestUtilsMedConnect.getSingleAppointment());
    }
}
