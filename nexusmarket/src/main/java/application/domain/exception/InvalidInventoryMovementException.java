package application.domain.exception;

/**
 * Thrown when an inventory movement is invalid (e.g. negative or zero
 * quantity, movement on a non-existent inventory).
 */
public class InvalidInventoryMovementException extends DomainException {

    public InvalidInventoryMovementException(String message) {
        super(message);
    }
}