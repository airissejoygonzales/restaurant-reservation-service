package ajag.projects.restaurant_reservation_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/** This class handles the expected exceptions thrown by the services to any controllers. */
@ControllerAdvice
public class GlobalExceptionHandler {

    /** This method catches the {@link NotFoundException} and gives a not found response with the exception message in return. */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /** This method catches the {@link InvalidValueException } and gives a bad request response with the exception message in return. */
    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<String> handleIllegalArgument(InvalidValueException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
