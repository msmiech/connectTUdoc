package at.connectTUdoc.backend.dao;

import at.connectTUdoc.backend.model.Appointment;
import at.connectTUdoc.backend.model.Office;
import at.connectTUdoc.backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


/**
 * This class represents the queries for the service layer to interact with the database.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * @param office - the office for the appointments
     * @return a list of appointments regarding the office
     */
    List<Appointment> findAppointmentsByOffice(Office office);

    /**
     * @param patient - the patient of the appointment
     * @param start - the start date of the appointment
     * @return a list of appointments fit the restriction
     */
    @Query("Select a from Appointment a where a.patient = :patient AND a.appointmentBegin >= :start")
    List<Appointment> findAppointmentsByPatient(@Param("patient") Patient patient, @Param("start") LocalDateTime start);

    /**
     * @param office - the office for the appointments
     * @param begin - the begin of the appointment
     * @param end - the end of the appointment
     * @return a list of appointments fit the restriction
     */
    List<Appointment> findAppointmentsByOfficeAndAppointmentBeginBetween(Office office, LocalDateTime begin, LocalDateTime end);

    /**
     * Deletes an appointment by the id
     * @param aLong - the id of the appointment
     */
    void deleteById(Long aLong);

    @Query("Select a from Appointment a where a.office = :office AND a.appointmentBegin >= :start AND a.appointmentEnd <= :end")
    List<Appointment> getAppointmentsBetweenDatesByOffice(@Param("office") Office office, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    /**
     * @param office office for which the appointments are retrieved
     * @param start start date of appointment used for comparison
     * @param end end date of appointment used for comparison
     * @return a list of appointments that overlap with the given start and end time for a specific office
     */
    @Query("Select a from Appointment a where a.office = :office AND (" +
            "(a.appointmentBegin <= :start AND a.appointmentEnd >= :end) OR " +
            "(a.appointmentBegin >= :start AND a.appointmentEnd <= :end) OR " +
            "(a.appointmentBegin > :start AND a.appointmentBegin < :end) OR " +
            "(a.appointmentEnd > :start AND a.appointmentEnd < :end))")
    List<Appointment> getOverlappingAppointmentsByOffice(@Param("office") Office office, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    /**
     * @param office office for which the appointments are retrieved
     * @param id id of the appointment with start and end time
     * @param start start date of appointment used for comparison
     * @param end end date of appointment used for comparison
     * @return a list of appointments that overlap with the given start and end time for a specific office excluding itself
     */
    @Query("Select a from Appointment a where a.office = :office AND a.id <> :id AND (" +
            "(a.appointmentBegin <= :start AND a.appointmentEnd >= :end) OR " +
            "(a.appointmentBegin >= :start AND a.appointmentEnd <= :end) OR " +
            "(a.appointmentBegin > :start AND a.appointmentBegin < :end) OR " +
            "(a.appointmentEnd > :start AND a.appointmentEnd < :end))")
    List<Appointment> getOverlappingAppointmentsByOfficeAndId(@Param("office") Office office, @Param("id") Long id, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    /**
     * Deletes an appointment by restriction of patient and office data
     * @param office - the office for the appointment
     * @param patient - the patient for the appointment
     */
    void deleteAppointmentByOfficeAndPatient(Office office, Patient patient);

}
