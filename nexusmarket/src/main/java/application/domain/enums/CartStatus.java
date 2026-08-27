package application.domain.enums;

/**
 * Status of a {@code Cart}. Checkout transforms a cart into a formal order.
 */
public enum CartStatus {
    OPEN,
    CHECKED_OUT
}