package at.connectTUdoc.backend.controller;

import at.connectTUdoc.backend.controller.abstracts.AbstractWebTest;
import at.connectTUdoc.backend.service.FirebaseService;
import com.google.firebase.auth.UserRecord;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

/**
 * This abstract class contains the patient controller web testing cases
 */
public class PatientControllerTest extends AbstractWebTest {

    protected static final String URI_USER_AUTH = "/patient/current";

    private static final String ID_TOKEN_HEADER_NAME = "X-Firebase-Auth";
    private static final String ID_TOKEN = "idtokentitis";
    private static final String NEW_FIREBASE_USER_UID = "newuid12345";
    private static final String NEW_FIREBASE_USER_MAIL = "hans@huber.com";
    private static final String NEW_FIREBASE_USER_NAME = "Herbert Huber";
    private static final String KNOWN_FIREBASE_USER_UID = "wCjBKGC7EMZo49VTMCDb92toPXf2";
    private static final String KNOWN_FIREBASE_USER_MAIL = "franz@patient.com";
    private static final String KNOWN_FIREBASE_USER_NAME = "Franz Walder";

    @MockBean
    private FirebaseService firebaseService;
    @MockBean
    private UserRecord userRecord;
    @MockBean
    Authentication authentication;
    @MockBean
    SecurityContext securityContext;

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void userAuth_shouldCreateNewMedconnectUserIfNotAlreadyPresent() throws Exception {
        Mockito.when(userRecord.getEmail()).thenReturn(NEW_FIREBASE_USER_MAIL);
        Mockito.when(userRecord.getDisplayName()).thenReturn(NEW_FIREBASE_USER_NAME);
        Mockito.when(firebaseService.getUserByUid(NEW_FIREBASE_USER_UID)).thenReturn(userRecord);
        Mockito.when(authentication.getName()).thenReturn(NEW_FIREBASE_USER_UID);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_USER_AUTH)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":4,\"uid\":\"newuid12345\",\"preTitle\":\"\",\"firstName\":\"Herbert\",\"lastName\":\"Huber\",\"posTitle\":\"\",\"eMail\":\"hans@huber.com\",\"privateKey\":null,\"publicKey\":null,\"svnr\":\"\"}", content);
    }

    @Test
    public void userAuth_shouldReturnUserIfAlreadyPresent() throws Exception {
        Mockito.when(userRecord.getEmail()).thenReturn(KNOWN_FIREBASE_USER_MAIL);
        Mockito.when(userRecord.getDisplayName()).thenReturn(KNOWN_FIREBASE_USER_NAME);
        Mockito.when(firebaseService.getUserByUid(KNOWN_FIREBASE_USER_UID)).thenReturn(userRecord);
        Mockito.when(authentication.getName()).thenReturn(KNOWN_FIREBASE_USER_UID);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI_USER_AUTH)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"id\":2,\"uid\":\"wCjBKGC7EMZo49VTMCDb92toPXf2\",\"preTitle\":\"\",\"firstName\":\"Franz\",\"lastName\":\"Walder\",\"posTitle\":\"Bsc.\",\"eMail\":\"franz@patient.com\",\"privateKey\":\"\",\"publicKey\":\"\",\"svnr\":\"2222111111\"}", content);
    }

}
