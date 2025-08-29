package co.edu.puj.regata.domain.exception;

public class EntityNotFoundException extends RegataException {
    
    public EntityNotFoundException(String entityName, Long id) {
        super(String.format("%s with ID %d not found", entityName, id));
    }
    
    public EntityNotFoundException(String message) {
        super(message);
    }
}