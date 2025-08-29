package co.edu.puj.regata.domain.exception;

public class RegataException extends RuntimeException {
    
    public RegataException(String message) {
        super(message);
    }
    
    public RegataException(String message, Throwable cause) {
        super(message, cause);
    }
}