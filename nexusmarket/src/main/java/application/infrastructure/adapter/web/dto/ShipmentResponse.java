package application.infrastructure.adapter.web.dto;

import application.domain.enums.ShipmentStatus;
import application.domain.model.Shipment;

import java.time.LocalDateTime;

/** Shipment view returned by the API (primitives only — no domain types). */
public record ShipmentResponse(Long id, Long orderId, Long warehouseId, String trackingNumber,
                               ShipmentStatus status, LocalDateTime shipmentDate,
                               LocalDateTime deliveryDate) {
    public static ShipmentResponse from(Shipment shipment) {
        return new ShipmentResponse(shipment.getId(), shipment.getOrderId(), shipment.getWarehouseId(),
                shipment.getTrackingNumber().getValue(), shipment.getStatus(),
                shipment.getShipmentDate(), shipment.getDeliveryDate());
    }
}
