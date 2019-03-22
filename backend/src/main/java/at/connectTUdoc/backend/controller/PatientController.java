package at.connectTUdoc.backend.controller;

import at.connectTUdoc.backend.dto.PatientDTO;
import at.connectTUdoc.backend.service.FirebaseService;
import at.connectTUdoc.backend.service.PatientService;
import com.google.firebase.FirebaseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This class handles the methods to communicate with the backend and the patient
 */
@Api(value = "api", description = "The Patient REST service")
@RestController
@RequestMapping("/patient")
public class PatientController {

    private static final Logger LOG = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    PatientService patientService;
    @Autowired
    FirebaseService firebaseService;
    @Autowired
    ModelMapper modelMapper;

    /**
     * This method retrieves a currently authenticated patient, if one is available.
     * Furthermore it creates a DB entry if the user is authenticated and indeed is a patient but not in db.
     *
     * @return PatientDTO of currently authenticated patient
     * @throws Exception
     */
    @GetMapping("/current")
    @ApiOperation(value = "Retrieve currently authenticated patient, if available", response = PatientDTO.class)
    @CrossOrigin 
    @ResponseBody
    public PatientDTO current() throws FirebaseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();

        LOG.info("User with uid: " + authentication.getName() + " is authenticated!");

        return patientService.retrieveCurrentPatient(uid);
    }

    /**
     * This method passes a registration token for a subscription
     * to receive notification
     *
     * @return true if subscription was successful
     * @throws Exception
     */
    @PostMapping("/subscribe")
    @ApiOperation(value = "Subscribes to notifications", response = Boolean.class)
    @CrossOrigin 
    @ResponseBody
    public Boolean subscribe(@Valid @RequestBody String registrationToken) throws FirebaseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();

        firebaseService.subscribeToTopic(registrationToken, uid);
        return true;
    }
}
