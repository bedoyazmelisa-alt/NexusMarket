package application.domain.enums;

/**
 * Whether a product is physical or digital. Physical products require inventory
 * and shipping; digital products can be delivered immediately after payment.
 */
public enum ProductType {
    PHYSICAL,
    DIGITAL
}