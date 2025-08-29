package co.edu.puj.regata.domain.exception;

public class ValidationException extends RegataException {
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String field, String value, String reason) {
        super(String.format("Validation failed for field '%s' with value '%s': %s", field, value, reason));
    }
}