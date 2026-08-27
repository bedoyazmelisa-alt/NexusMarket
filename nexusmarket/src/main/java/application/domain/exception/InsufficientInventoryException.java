package application.domain.exception;

/**
 * Thrown when the requested quantity exceeds the available inventory (RG-08).
 */
public class InsufficientInventoryException extends DomainException {

    public InsufficientInventoryException(String message) {
        super(message);
    }
}