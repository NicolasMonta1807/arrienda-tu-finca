package web.mates.arriendatufinca.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Object> handleDuplicateEmailException(DuplicateEmailException ex) {
        return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
            put("error", ex.getMessage());
        }});
    }

    @ExceptionHandler(DuplicateMunicipalityException.class)
    public ResponseEntity<Object> handleDuplicateMunicipalityException(DuplicateMunicipalityException ex) {
        return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
            put("error", ex.getMessage());
        }});
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        return new HashMap<>() {{
            put("errors", errors);
        }};
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<Object> handleInvalidDateException(InvalidDateException ex) {
        return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
            put("error", ex.getMessage());
        }});
    }

    @ExceptionHandler(InvalidBookingStateException.class)
    public ResponseEntity<Object> handleInvalidBookingStateException(InvalidBookingStateException ex) {
        return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
            put("error", ex.getMessage());
        }});
    }
}
