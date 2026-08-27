package application.domain.exception;

/**
 * Thrown when an attempt is made to modify a finalized order (RG-12).
 */
public class FinalizedOrderModificationException extends DomainException {

    public FinalizedOrderModificationException(String message) {
        super(message);
    }
}