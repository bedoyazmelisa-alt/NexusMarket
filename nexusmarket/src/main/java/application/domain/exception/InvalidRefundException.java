package application.domain.exception;

/**
 * Thrown when a refund cannot be processed according to business rules.
 */
public class InvalidRefundException extends DomainException {

    public InvalidRefundException(String message) {
        super(message);
    }
}