package application.domain.exception;

/**
 * Thrown when a seller attempts to self-register (RG-04) or when the acting
 * user is not an Administrator (RG-05).
 */
public class SellerRegistrationNotAllowedException extends DomainException {

    public SellerRegistrationNotAllowedException(String message) {
        super(message);
    }
}