package application.domain.exception;

/**
 * Thrown when an operation requires a valid {@code ProductStatus} but the
 * current state does not allow it.
 */
public class InvalidProductStateException extends DomainException {

    public InvalidProductStateException(String message) {
        super(message);
    }
}