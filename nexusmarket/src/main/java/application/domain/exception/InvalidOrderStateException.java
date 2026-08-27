package application.domain.exception;

/**
 * Thrown when an order does not support the requested state transition
 * or an operation requires a different order state (RG-10).
 */
public class InvalidOrderStateException extends DomainException {

    public InvalidOrderStateException(String message) {
        super(message);
    }
}