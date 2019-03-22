package at.connectTUdoc.backend.service;

import at.connectTUdoc.backend.dto.AppointmentDTO;
import at.connectTUdoc.backend.model.Office;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

/**
 * This class represents the services to use for communication with the database.
 */
public interface AppointmentService {

    /**
     * @param office id of the office for which the appointments are retrieved
     * @return list of all appointments for the given office
     */
    List<AppointmentDTO> findAppointmentsByOffice(Office office);

    /**
     * @param officeId id of the office for which the appointments are returned
     * @param date date for which the appointments are returned
     * @return all available appointments for a given office and date
     */
    List<AppointmentDTO> getAvailableAppointmentsByOfficeAndDate(Long officeId, String date);

    /**
     * @return returns a list of appointments for the currently logged in user
     */
    List<AppointmentDTO> getAllAppointmentsOfLoggedInUser();

    /**
     * @param appointment - object which should be stored in the DB
     * @return the appointment object stored in the DB with a valid id
     */
    AppointmentDTO createAppointment(AppointmentDTO appointment) throws ResponseStatusException;


    void deleteAppointmentById(Long id);

    /**
     * Updates an existing appointment.
     *
     * @param appointment - contain the id to identify the appointment and the start and end time to be updated
     * @return the updated appointment object
     */
    AppointmentDTO updateAppointment(AppointmentDTO appointment) throws ResponseStatusException;

    /**
     * Gets all appointments for a specific office between the given dates.
     * @param officeId id of the office for which the appointments are retrieved
     * @param startDate start date for the appointment list
     * @param endDate end date for the appointment list
     * @return list of appointments for the given parameters
     */
    List<AppointmentDTO> getAppointmentBetweenDatesByOffice(Long officeId, String startDate, String endDate);

}
