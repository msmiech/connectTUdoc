package at.connectTUdoc.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * It’s thrown when an unexpected situation occurs while authenticate a user
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotAuthenticatedException extends AuthenticationException {

  public UserNotAuthenticatedException(String message) {
    super(message);
  }

  public UserNotAuthenticatedException(String message, Exception cause) {
    super(message, cause);
  }

}
