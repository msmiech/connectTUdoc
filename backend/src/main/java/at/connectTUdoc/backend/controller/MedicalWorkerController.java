package at.connectTUdoc.backend.controller;

import at.connectTUdoc.backend.dto.MedicalWorkerDTO;
import at.connectTUdoc.backend.service.MedicalWorkerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * This class handles the methods to communicate with the backend and the medical worker
 */
@Api(value = "api", description = "The MedicalWorker REST service")
@RestController
@RequestMapping("/medicalworker")
public class MedicalWorkerController {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalWorkerController.class);

    @Autowired
    MedicalWorkerService medicalWorkerService;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/fire")
    @ApiOperation(value = "Get medical worker id with the uid")
    @CrossOrigin
    @ResponseBody
    public MedicalWorkerDTO getMedicalWorkerID() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        return medicalWorkerService.findMedicalWorkerByUID(uid);
    }
}
