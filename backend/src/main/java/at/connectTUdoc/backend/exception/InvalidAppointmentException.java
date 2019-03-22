package at.connectTUdoc.backend.exception;

/**
 * It’s thrown when an unexpected situation occurs while storing an appointment to the database
 */
public class InvalidAppointmentException extends Exception {
    public InvalidAppointmentException(String message) {
        super(message);
    }

    public InvalidAppointmentException(String message, Exception cause) {
        super(message, cause);
    }
}
