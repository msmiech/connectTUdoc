package at.connectTUdoc.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * It’s thrown when an unexpected situation occurs while processing an medicine
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MedConnectException extends Exception {

  public MedConnectException(String message) {
    super(message);
  }

  public MedConnectException(String message, Exception cause) {
    super(message, cause);
  }

}
