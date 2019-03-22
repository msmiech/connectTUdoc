package at.connectTUdoc.backend.controller;

import at.connectTUdoc.backend.dto.MedicineDTO;
import at.connectTUdoc.backend.service.MedicineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This class handles the methods to communicate with the backend and the medicine
 */
@Api(value = "api", description = "The Medicine REST service")
@RestController
@RequestMapping("/medicine")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/{id}")
    @ApiOperation(value = "Get the medicine for the given id", response = MedicineDTO.class)
    @CrossOrigin
    public MedicineDTO findMedicineById(@PathVariable("id") String id) {
        return medicineService.getMedicineById(id);
    }
}
