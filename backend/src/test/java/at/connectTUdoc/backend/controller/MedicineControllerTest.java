package at.connectTUdoc.backend.controller;

import at.connectTUdoc.backend.controller.abstracts.AbstractWebTest;
import at.connectTUdoc.backend.service.FirebaseService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

/**
 * This class hold the controller tests
 */
public class MedicineControllerTest extends AbstractWebTest {
    private static final String URI_MEDICINE = "/medicine";

    @MockBean
    Authentication authentication;
    @MockBean
    SecurityContext securityContext;
    @MockBean
    FirebaseService firebaseService;

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void findMedicineById_ThrowsResourceNotFoundException() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_MEDICINE + "/0001371")).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void findMedicineById_ReturnsOneMedicine() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_MEDICINE + "/0001370")).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String  content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":\"0001370\",\"name\":\"Aknichthol Lotion\"}",content);
    }
}
