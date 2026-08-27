package application.domain.exception;

/**
 * Thrown when an operation attempts to reserve inventory that is damaged (RG-09).
 */
public class DamagedInventoryException extends DomainException {

    public DamagedInventoryException(String message) {
        super(message);
    }
}