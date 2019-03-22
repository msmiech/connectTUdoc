package at.connectTUdoc.backend.service;

import at.connectTUdoc.backend.exception.UserNotAuthenticatedException;
import at.connectTUdoc.backend.service.impl.FirebaseServiceImpl;
import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * Service Tests for CRUD operations are no benefit, cause there is no ongoing manipulation threw the service-layer.
 * For example some methods are shown to show how to mock these methods.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*"})
@PrepareForTest({FirebaseAuth.class, FirebaseToken.class})
public class FirebaseServiceTest {

    private static final String ID_TOKEN = "123456789";
    private static final String USER_ID = "987654321";

    @TestConfiguration
    static class FirebaseServiceTestContextConfiguration {

        @Bean
        public FirebaseService firebaseService() {
            return new FirebaseServiceImpl();
        }

    }

    @Autowired
    FirebaseService firebaseService;

    private FirebaseAuth firebaseAuth;
    private ApiFuture apiFuture;
    private FirebaseToken firebaseToken;
    private UserRecord userRecord;

    @Before
    public void setUP() {
        PowerMockito.mockStatic(FirebaseAuth.class);
        firebaseToken = PowerMockito.mock(FirebaseToken.class);
        firebaseAuth = PowerMockito.mock(FirebaseAuth.class);
        apiFuture = PowerMockito.mock(ApiFuture.class);
        userRecord = PowerMockito.mock(UserRecord.class);

        PowerMockito.when(FirebaseAuth.getInstance()).thenReturn(firebaseAuth);
    }

    @Test
    public void getUserIdFromIdToken_Correct() throws Exception {
        PowerMockito.when(firebaseToken.getUid()).thenReturn(USER_ID);
        PowerMockito.when(apiFuture.get()).thenReturn(firebaseToken);
        PowerMockito.when(firebaseAuth.verifyIdTokenAsync(ID_TOKEN)).thenReturn(apiFuture);

        assertEquals(USER_ID, firebaseService.getUserIdFromIdToken(ID_TOKEN));
    }

    @Test(expected = UserNotAuthenticatedException.class)
    public void getUserIdFromIdToken_ThrowsUserNotAuthenticatedException() throws Exception {
        PowerMockito.when(apiFuture.get()).thenThrow(ExecutionException.class);
        PowerMockito.when(firebaseAuth.verifyIdTokenAsync(ID_TOKEN)).thenReturn(apiFuture);

        firebaseService.getUserIdFromIdToken(ID_TOKEN);
    }

    @Test
    public void getUserByUid_Correct() throws Exception {
        PowerMockito.when(userRecord.getUid()).thenReturn(USER_ID);
        PowerMockito.when(firebaseAuth.getUser(USER_ID)).thenReturn(userRecord);

        assertEquals(USER_ID, firebaseService.getUserByUid(USER_ID).getUid());
    }

    @Test(expected = FirebaseAuthException.class)
    public void getUserByUid_ThrowsFirebaseAuthException() throws Exception {
        PowerMockito.when(firebaseAuth.getUser(USER_ID)).thenThrow(FirebaseAuthException.class);

        firebaseService.getUserByUid(USER_ID);
    }

}
