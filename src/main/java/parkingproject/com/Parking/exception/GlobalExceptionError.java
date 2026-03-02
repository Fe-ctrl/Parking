package parkingproject.com.Parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionError {

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