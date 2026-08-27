package application.domain.enums;

/**
 * Business event types that change inventory quantities.
 */
public enum InventoryMovementType {
    ENTRY,
    RESERVATION,
    SALE,
    ADJUSTMENT,
    RETURN
}