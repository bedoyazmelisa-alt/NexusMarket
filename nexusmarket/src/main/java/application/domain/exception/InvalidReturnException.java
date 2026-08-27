package application.domain.exception;

/**
 * Thrown when a return request is invalid according to business rules.
 */
public class InvalidReturnException extends DomainException {

    public InvalidReturnException(String message) {
        super(message);
    }
}