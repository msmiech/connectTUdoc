package at.connectTUdoc.backend.controller;

import at.connectTUdoc.backend.dto.AppointmentDTO;
import at.connectTUdoc.backend.model.Appointment;
import at.connectTUdoc.backend.service.AppointmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * This class handles the methods to communicate with the backend and the appointments
 */
@Api(value = "api", description = "The Appointment REST service")
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/available/{officeId}/{date}")
    @ApiOperation(value = "Get a list of all available appointments for a given date and office", response = AppointmentDTO.class, responseContainer = "List")
    @CrossOrigin
    @ResponseBody
    public List<AppointmentDTO> getAllAvailableAppointmentsByDateAndOffice(@PathVariable("officeId") Long officeId, @PathVariable("date") String date) {
        List<AppointmentDTO> appointmentDTOList = appointmentService.getAvailableAppointmentsByOfficeAndDate(officeId, date);
        return appointmentDTOList;
    }

    @GetMapping("/patient")
    @ApiOperation(value = "Get a list of all appointments of the the currently logged in user", response = AppointmentDTO.class, responseContainer = "List")
    @CrossOrigin
    @ResponseBody
    public List<AppointmentDTO> getAllAppointmentsOfPatient() {
        List<AppointmentDTO> appointmentDTOList = appointmentService.getAllAppointmentsOfLoggedInUser();
        return appointmentDTOList;
    }


    /**
     * This method acts as save/create method
     *
     * @param appointment
     * @return
     */
    @PostMapping
    @ApiOperation(value = "Create a new appointment", response = Boolean.class)
    @CrossOrigin
    @ResponseBody
    public Boolean postSignupForAppointment(@RequestBody AppointmentDTO appointment)  {
        return appointmentService.createAppointment(appointment) != null;
    }

    @PutMapping
    @ApiOperation(value = "Update an appointment identified by its ID", response = Appointment.class)
    @CrossOrigin
    public AppointmentDTO updateAppointment(@Valid @RequestBody AppointmentDTO appointment) {
        return appointmentService.updateAppointment(appointment);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete an appointment by its id")
    @CrossOrigin
    public void deleteAppointment(@PathVariable("id") Long id) {
        appointmentService.deleteAppointmentById(id);
    }

    @GetMapping("/office/{id}")
    @ApiOperation(value = "Get a list of appointments by office", response = Appointment.class, responseContainer = "List")
    @CrossOrigin
    public List<AppointmentDTO> getAppointmentsByOffice(@PathVariable("id") Long officeId, @RequestParam("start") String start, @RequestParam("end") String end) {
        List<AppointmentDTO> dto = appointmentService.getAppointmentBetweenDatesByOffice(officeId, start,end);
        return dto;

    }

}
