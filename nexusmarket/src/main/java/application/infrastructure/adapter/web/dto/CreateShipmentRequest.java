package application.infrastructure.adapter.web.dto;

/** Request to create a shipment. */
public record CreateShipmentRequest(Long orderId, Long warehouseId, String trackingNumber) {
}
