package application.domain.exception;

/**
 * Base class for all business rule violations in the NexusMarket domain.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}