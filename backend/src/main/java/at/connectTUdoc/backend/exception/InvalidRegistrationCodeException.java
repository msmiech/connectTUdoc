package at.connectTUdoc.backend.exception;

/**
 * It’s thrown when an unexpected situation occurs while validate an registration code
 */
public class InvalidRegistrationCodeException extends Exception {
    public InvalidRegistrationCodeException(String message) {
        super(message);
    }

    public InvalidRegistrationCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
