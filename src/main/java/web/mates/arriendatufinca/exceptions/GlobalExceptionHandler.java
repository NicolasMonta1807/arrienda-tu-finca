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

    static final String ERROR_KEY = "error";

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Object> handleDuplicateEmailException(DuplicateEmailException ex) {
        HashMap<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(DuplicateMunicipalityException.class)
    public ResponseEntity<Object> handleDuplicateMunicipalityException(DuplicateMunicipalityException ex) {
        HashMap<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        HashMap<String, List<String>> errorsMap = new HashMap<>();
        errorsMap.put(ERROR_KEY, errors);
        return errorsMap;
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<Object> handleInvalidDateException(InvalidDateException ex) {
        HashMap<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidBookingStateException.class)
    public ResponseEntity<Object> handleInvalidBookingStateException(InvalidBookingStateException ex) {
        HashMap<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        HashMap<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(401).body(error);
    }
}
