package at.connectTUdoc.backend.config.security;

import at.connectTUdoc.backend.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * This class holds the needed method for firebase provider
 */
@Component
public class FirebaseAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private FirebaseService firebaseService;

    @Override
    public boolean supports(Class<?> authentication) {
        return (FirebaseAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        FirebaseAuthenticationToken authenticationToken = (FirebaseAuthenticationToken) authentication;

        String uid = firebaseService.getUserIdFromIdToken(authenticationToken.getCredentials().toString());

        return new FirebaseAuthenticationToken(uid, authentication.getCredentials(), null);
    }
}
