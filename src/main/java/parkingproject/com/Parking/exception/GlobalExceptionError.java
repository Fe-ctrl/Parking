package parkingproject.com.Parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionError {

    // Missing or wrong query params (e.g. forgot to send ?plate=...)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Missing Parameter");
        response.put("details", "Required parameter '" + ex.getParameterName() + "' is missing");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Wrong type for a param (e.g. type=abc instead of type=1)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Invalid Parameter");
        response.put("details", "Parameter '" + ex.getName() + "' has an invalid value: " + ex.getValue());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // All other exceptions (DB errors, etc.)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        String errorMessage = ex.getMessage();
        if (errorMessage != null && errorMessage.contains("ORA-")) {
            response.put("error", "Database Error");
            response.put("details", extractOracleError(errorMessage));
        } else {
            response.put("error", "Internal Server Error");
            response.put("details", ex.getMessage());
        }

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String extractOracleError(String message) {
        int start = message.indexOf("ORA-");
        if (start != -1) {
            int end = message.indexOf("\n", start);
            return (end != -1) ? message.substring(start, end) : message.substring(start);
        }
        return message;
    }
}