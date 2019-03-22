package at.connectTUdoc.backend.controller;

import at.connectTUdoc.backend.controller.abstracts.AbstractWebTest;
import at.connectTUdoc.backend.dto.MedicalWorkerDTO;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

/**
 * This abstract class contains the medical worker controller web testing cases
 */
public class MedicalWorkerControllerTest extends AbstractWebTest {

    protected static final String URI_CREATE_MEDICAL_WORKER = "/medicalworker";
    protected static final String URI_FIND_ALL_MEDICAL_WORKERS = "/medicalworker";

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void createMedicalWorker_NewMedicalWorkerInDatabase() throws Exception {
        MedicalWorkerDTO medicalWorker = TestUtilsMedConnect.getSingleMedicalWorkerDTO();
        
        String inputJson = super.mapToJson(medicalWorker);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URI_CREATE_MEDICAL_WORKER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        
        // Validate the correct 
        MedicalWorkerDTO resp = super.mapFromJson(content,MedicalWorkerDTO.class);
        // innsert ID´s
        setDatabaseReferenceIdsToGeneratedObject(resp,medicalWorker);

        assertEquals(resp, medicalWorker);
    }

    @Test
    public void findAllMedicalWorker_Find6MedicalWorkers() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_FIND_ALL_MEDICAL_WORKERS)
                ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("[{" +
                "\"id\":1,\"uid\":\"bS9nqS956xUFYYy26bWmFvT8noo1\",\"preTitle\":\"ire\",\"firstName\":\"Marcus\",\"lastName\":\"Mayer\",\"posTitle\":\"Vamp\",\"eMail\":\"graf@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\""+
                ",\"specialities\":[{\"id\":1,\"specialityName\":\"Gynäkologe\"}],\"type\":\"DOCTOR\"},"+
                "{\"id\":2,\"uid\":\"7EWSTzVnlqgb7bC0dPbjAdmZUV12\",\"preTitle\":\"tent\",\"firstName\":\"Max\",\"lastName\":\"Muster\",\"posTitle\":\"Ass\",\"eMail\":\"max.muster@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\""+
                ",\"specialities\":[{\"id\":2,\"specialityName\":\"Technician\"}],\"type\":\"ASSISTANT\"},"+
                "{\"id\":3,\"uid\":\"PmgPFW3CsZXht2IuOFiAqldozVV2\",\"preTitle\":\"MSc\",\"firstName\":\"Berta\",\"lastName\":\"Hadel\",\"posTitle\":\"Dr\",\"eMail\":\"berta.hadel@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\""+
                ",\"specialities\":[{\"id\":3,\"specialityName\":\"Dentist\"}],\"type\":\"DOCTOR\"},"+
                "{\"id\":4,\"uid\":\"igi3pmYNRRWeQurhojiX8fDJmGj2\",\"preTitle\":\"\",\"firstName\":\"Mist\",\"lastName\":\"Eria\",\"posTitle\":\"Dipl.Ing\",\"eMail\":\"mist.eria@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\""+
                ",\"specialities\":[{\"id\":4,\"specialityName\":\"Common Allergic\"}],\"type\":\"ASSISTANT\"},"+
                "{\"id\":5,\"uid\":\"Ea3D4hYZjAU8UNMM3JYpwACMI303\",\"preTitle\":\"\",\"firstName\":\"Manuel\",\"lastName\":\"Meister\",\"posTitle\":\"Dr.\",\"eMail\":\"manuel.meister@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\"" +
                ",\"specialities\":[{\"id\":5,\"specialityName\":\"Technician\"}],\"type\":\"DOCTOR\"},"+
                "{\"id\":6,\"uid\":\"V3ZYOd3fn5b8q7INqUG06Akh5uI3\",\"preTitle\":\"\",\"firstName\":\"Eva\",\"lastName\":\"Jung\",\"posTitle\":\"Dr.\",\"eMail\":\"eva.jung@dracu.la\",\"privateKey\":\"\",\"publicKey\":\"\""+
                ",\"specialities\":[{\"id\":6,\"specialityName\":\"Psychology\"}],\"type\":\"DOCTOR\"}]",content);
    }
}
