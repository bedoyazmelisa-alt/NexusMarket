package application.domain.exception;

/**
 * Thrown when an operation requires a valid {@code UserStatus} but the
 * current state does not allow it.
 */
public class InvalidUserStatusException extends DomainException {

    public InvalidUserStatusException(String message) {
        super(message);
    }
}