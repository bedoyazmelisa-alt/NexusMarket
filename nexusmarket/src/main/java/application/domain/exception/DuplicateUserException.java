package application.domain.exception;

/**
 * Thrown when a user registration violates one of the uniqueness rules:
 * RG-13 (identification) or RG-14 (email).
 */
public class DuplicateUserException extends DomainException {

    public DuplicateUserException(String message) {
        super(message);
    }
}
