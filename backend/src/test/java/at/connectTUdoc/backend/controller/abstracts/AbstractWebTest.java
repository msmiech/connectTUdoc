package at.connectTUdoc.backend.controller.abstracts;

import at.connectTUdoc.backend.BackendApplication;
import at.connectTUdoc.backend.dto.MedicalWorkerDTO;
import at.connectTUdoc.backend.dto.OfficeDTO;
import at.connectTUdoc.backend.service.MedicalWorkerService;
import at.connectTUdoc.backend.service.OfficeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;

/**
 * This abstract class contains the common used variables and methods for controller web testing
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public abstract class AbstractWebTest {

    @Autowired
    protected  ObjectMapper objectMapper;

    protected MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    protected OfficeService officeService;

    @Autowired
    protected MedicalWorkerService medicalWorkerService;

    protected void setUp() {

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    protected void setDatabaseReferenceIdsToGeneratedObject(MedicalWorkerDTO databaseObj, MedicalWorkerDTO generatedObj){
        // Set ids cause of database insertion
        generatedObj.setId(databaseObj.getId());
        if(databaseObj.getSpecialities() != null && generatedObj.getSpecialities() != null) {
            for (int i = 0; i < databaseObj.getSpecialities().size(); i++) {
                generatedObj.getSpecialities().get(i).setId(databaseObj.getSpecialities().get(i).getId());
            }
        }
    }

    private void setDatabaseReferenceIdsToGeneratedObject(OfficeDTO databaseObj, OfficeDTO generatedObj){
        // Set ids cause of database insertion
        generatedObj.setId(databaseObj.getId());
        if(databaseObj.getAddress() != null && generatedObj.getAddress() != null) {
            generatedObj.getAddress().setId(databaseObj.getAddress().getId());
        }
        if(databaseObj.getAppointments() != null && generatedObj.getAppointments() != null) {
            for (int i = 0; i < databaseObj.getAppointments().size(); i++) {
                generatedObj.getAppointments().get(i).setId(databaseObj.getAppointments().get(i).getId());
                generatedObj.getAppointments().get(i).setOffice(databaseObj.getAppointments().get(i).getOffice());
            }
        }
        if(databaseObj.getOfficehours() != null && generatedObj.getOfficehours() != null) {
            for (int i = 0; i < databaseObj.getOfficehours().size(); i++) {
                generatedObj.getOfficehours().get(i).setId(databaseObj.getOfficehours().get(i).getId());
            }
        }
        if(databaseObj.getOfficeWorkers() != null && generatedObj.getOfficeWorkers() != null) {
            for (int i = 0; i < databaseObj.getOfficeWorkers().size(); i++) {
                setDatabaseReferenceIdsToGeneratedObject(databaseObj.getOfficeWorkers().get(i), generatedObj.getOfficeWorkers().get(i));
            }
        }
    }

    protected void setDatabaseReferenceIdToGeneratedListObjects(List<OfficeDTO> databaseObj, List<OfficeDTO> generatedObj){
        for (int i = 0; i < databaseObj.size(); i++) {
            setDatabaseReferenceIdsToGeneratedObject(databaseObj.get(i), generatedObj.get(i));
        }

    }
}

