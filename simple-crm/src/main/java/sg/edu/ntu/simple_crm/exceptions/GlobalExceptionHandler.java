package sg.edu.ntu.simple_crm.exceptions;

// Provides the date and time used in each error response.
import java.time.LocalDateTime;
// Provides the List collection used for validation errors.
import java.util.List;

// Represents HTTP status codes such as 400, 404, and 500.
import org.springframework.http.HttpStatus;
// Wraps an HTTP response body together with its status code.
import org.springframework.http.ResponseEntity;
// Represents validation failures raised when a request body is invalid.
import org.springframework.web.bind.MethodArgumentNotValidException;
// Marks a method as responsible for handling a particular exception type.
import org.springframework.web.bind.annotation.ExceptionHandler;
// Applies this exception handler to all REST controllers in the application.
import org.springframework.web.bind.annotation.RestControllerAdvice;
// Represents one validation error from Spring's binding result.
import org.springframework.validation.ObjectError;

// Declares a global exception handler for REST API errors.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles requests for customers that cannot be found.
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        // Create an error body containing the exception message and current time.
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), LocalDateTime.now());
        // Return the error body with HTTP 404 Not Found.
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCustomerIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCustomerIdException(InvalidCustomerIdException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handles unexpected exceptions that are not handled more specifically.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        // Create a generic error body without exposing internal implementation details.
        ErrorResponse errorResponse = new ErrorResponse("Something went wrong!", LocalDateTime.now());
        // Return the error body with HTTP 500 Internal Server Error.
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handles validation failures for request bodies annotated with validation
    // rules.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {

        // Get all validation errors collected during request-body binding.
        List<ObjectError> validationErrors = exception.getBindingResult().getAllErrors();

        // Create a builder so all validation messages can be returned together.
        StringBuilder sb = new StringBuilder();
        // Visit each validation error and append its human-readable message.
        for (ObjectError error : validationErrors) {
            // Add punctuation and spacing between individual validation messages.
            sb.append(error.getDefaultMessage()).append(". ");
        }

        // Create an error body containing all validation messages and the current time.
        ErrorResponse errorResponse = new ErrorResponse(sb.toString(), LocalDateTime.now());
        // Return the validation details with HTTP 400 Bad Request.
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
