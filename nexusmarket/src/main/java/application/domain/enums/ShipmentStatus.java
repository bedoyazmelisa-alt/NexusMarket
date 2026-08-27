package application.domain.enums;

/**
 * Logistics lifecycle of a {@code Shipment}.
 */
public enum ShipmentStatus {
    PREPARING,
    DISPATCHED,
    IN_TRANSIT,
    DELIVERED
}