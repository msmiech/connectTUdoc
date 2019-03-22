package at.connectTUdoc.backend.controller;

import at.connectTUdoc.backend.dto.MedicalWorkerDTO;
import at.connectTUdoc.backend.dto.OfficeDTO;
import at.connectTUdoc.backend.dto.PatientDTO;
import at.connectTUdoc.backend.dto.RegistrationCodeDTO;
import at.connectTUdoc.backend.service.FirebaseService;
import at.connectTUdoc.backend.service.MedicalWorkerService;
import at.connectTUdoc.backend.service.OfficeService;
import at.connectTUdoc.backend.service.PatientService;
import com.google.firebase.FirebaseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles the methods to communicate with the backend and the office
 */
@Api(value = "api", description = "The Office REST service")
@RestController
@RequestMapping("/office")
public class OfficeController {

    private static final Logger LOG = LoggerFactory.getLogger(OfficeController.class);

    @Autowired
    OfficeService officeService;
    @Autowired
    MedicalWorkerService medicalWorkerService;
    @Autowired
    PatientService patientService;
    @Autowired
    FirebaseService firebaseService;
    @Autowired
    private ModelMapper modelMapper;


    // --------------- office --------------------
    @GetMapping()
    @ApiOperation(value = "Get a list of all offices", response = OfficeDTO.class, responseContainer = "List")
    @CrossOrigin
    @ResponseBody
    public List<OfficeDTO> findAllOffices() {
        return Arrays.asList(modelMapper.map(officeService.findAllOffices(), OfficeDTO[].class));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get an office by its id", response = OfficeDTO.class)
    @CrossOrigin
    @ResponseBody
    public OfficeDTO findOfficeById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        return officeService.findOfficeById(id);
    }

    @PostMapping()
    @ApiOperation(value = "Register a new office", response = OfficeDTO.class)
    @CrossOrigin
    @ResponseBody
    public OfficeDTO createOffice(@Valid @RequestBody OfficeDTO officeDTO) {
        return officeService.createOffice(officeDTO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete an office by its id")
    @CrossOrigin
    public void deleteOfficeById(@PathVariable("id") Long id) {
        officeService.deleteOfficeById(id);
    }

    @PutMapping()
    @ApiOperation(value = "Update an office identified by its ID", response = OfficeDTO.class)
    @CrossOrigin
    @ResponseBody
    public OfficeDTO updateOffice(@Valid @RequestBody OfficeDTO officeDTO) {
        System.out.println("Update");
        return officeService.updateOffice(officeDTO);
    }

    @GetMapping("/searchText/{searchText}")
    @ApiOperation(value = "Find an office by his name, doctors name or name of the speciality", response = OfficeDTO.class, responseContainer = "List")
    @CrossOrigin
    @ResponseBody
    public List<OfficeDTO> getOfficeBySearchText(@PathVariable("searchText") String searchText) {
        List<OfficeDTO> ret = new ArrayList<>();
        ret.addAll(officeService.findOfficesByName(searchText));
        ret.addAll(officeService.getOfficeBySearchTextDoctorNameOnly(searchText));
        ret.addAll(officeService.getOfficeBySearchTextSpecialityNameOnly(searchText));
        return ret;
    }

    @GetMapping("/searchTextNameOnly/{searchText}")
    @ApiOperation(value = "Find an office by his name only", response = OfficeDTO.class, responseContainer = "List")
    @CrossOrigin
    @ResponseBody
    public List<OfficeDTO> getOfficeBySearchTextOfficeNameOnly(@PathVariable String searchText) {
        return officeService.getOfficeBySearchTextOfficeNameOnly(searchText);
    }

    @GetMapping("/searchTextSpecialityNameOnly/{searchText}")
    @ApiOperation(value = "Find an office by workers speciality only", response = OfficeDTO.class, responseContainer = "List")
    @CrossOrigin
    @ResponseBody
    public List<OfficeDTO> getOfficeBySearchTextSpecialityNameOnly(@PathVariable String searchText) {
        return officeService.getOfficeBySearchTextSpecialityNameOnly(searchText);
    }

    @GetMapping("/searchTextDoctorNameOnly/{searchText}")
    @ApiOperation(value = "Find an office by doctors name only", response = OfficeDTO.class, responseContainer = "List")
    @CrossOrigin
    @ResponseBody
    public List<OfficeDTO> getOfficeBySearchTextDoctorNameOnly(@PathVariable String searchText) {
        return officeService.getOfficeBySearchTextDoctorNameOnly(searchText);
    }

    @GetMapping("/searchOfficeByMedicalWorker/{medicalWorkerID}")
    @ApiOperation(value = "Get an office by assigned medical worker", response = OfficeDTO.class)
    @CrossOrigin
    @ResponseBody
    public OfficeDTO getOfficeByMedicalWorker(@PathVariable int medicalWorkerID) {
        MedicalWorkerDTO medicalWorkerDTO = modelMapper.map(medicalWorkerService.findMedicalWorkerById(medicalWorkerID), MedicalWorkerDTO.class);
        return officeService.findOfficeByMedicalWorker(medicalWorkerDTO);
    }

    @GetMapping("/searchOfficeByLoggedInMedicalUser")
    @ApiOperation(value = "Get an office by assigned medical worker who is currently logged in", response = OfficeDTO.class)
    @CrossOrigin
    @ResponseBody
    public OfficeDTO getOfficeByMedicalWorkerLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MedicalWorkerDTO medicalWorkerDTO = modelMapper.map(medicalWorkerService.findMedicalWorkerByUID(authentication.getName()),MedicalWorkerDTO.class);
        return officeService.findOfficeByMedicalWorker(medicalWorkerDTO);
    }

    @GetMapping("/patientOffices")
    @ApiOperation(value = "Get all offices by patient")
    @CrossOrigin
    @ResponseBody
    public List<OfficeDTO> getOfficesByPatient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        PatientDTO patient = patientService.findPatientByUid(authentication.getName());

        return officeService.getOfficesByPatient(patient);
    }

    @GetMapping("/officePatients")
    @ApiOperation(value = "Get all patients by office")
    @CrossOrigin
    @ResponseBody
    public List<PatientDTO> getPatientsToLoggedInUser() {
        OfficeDTO officeDTO = getOfficeByMedicalWorkerLoggedIn();
        return officeService.getPatientsByOfficeId(officeDTO.getId());
    }

    /**
     * This method passes a registration token to subscribe to notifications for this office
     *
     * @return true if subscription was successful
     * @throws Exception
     */
    @PostMapping("/subscribe")
    @ApiOperation(value = "Subscribes to notifications", response = Boolean.class)
    @CrossOrigin
    @ResponseBody
    public Boolean subscribe(@Valid @RequestBody String registrationToken) throws FirebaseException {
        OfficeDTO officeDTO = getOfficeByMedicalWorkerLoggedIn();
        firebaseService.subscribeToTopic(registrationToken, "office-" + officeDTO.getId());
        return true;
    }

    @GetMapping("/{id}/patients/unconfirmed")
    @ApiOperation(value = "Get all unconfirmed patients by office")
    @CrossOrigin
    @ResponseBody
    public List<PatientDTO> getUnconfirmedPatients(@PathVariable Long id) {
        return officeService.getUnconfirmedPatients(id);
    }

    @DeleteMapping("/{officeid}/patient/{patientid}")
    @ApiOperation(value = "Declines a patient for the office")
    @CrossOrigin
    public void deleteAppointmentsByOfficeANDPatient(@PathVariable("officeid") Long officeid, @PathVariable("patientid") Long patientid) {
        officeService.deleteAppointmentsByOfficeANDPatient(officeid,patientid);
    }
    @PutMapping("/{officeid}/patient/{patientid}")
    @ApiOperation(value = "Adds a patient to an office", response = OfficeDTO.class)
    @CrossOrigin
    @ResponseBody
    public void addOffice(@PathVariable("officeid") Long officeid, @PathVariable("patientid") Long patientid, @Valid @RequestBody RegistrationCodeDTO code) {
        officeService.addPatientToOffice(officeid,patientid,code);
    }

    @GetMapping("/patient/registrationcode")
    @ApiOperation(value = "Returns a new code to register for an office")
    @CrossOrigin
    @ResponseBody
    public RegistrationCodeDTO getRegistrationCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PatientDTO patient = patientService.findPatientByUid(authentication.getName());

        RegistrationCodeDTO code = officeService.getRegistrationCode(patient.getId());
        return code;
    }
}
