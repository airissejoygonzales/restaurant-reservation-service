package ajag.projects.restaurant_reservation_service.exceptions;

/** This is the exception to be thrown when customer or reservation with the provided parameter doesn't exist in the database. */
public class InvalidValueException extends RuntimeException {
    public InvalidValueException(String message) {
        super(message);
    }
}
