package application.domain.model;

import application.domain.enums.ShipmentStatus;
import application.domain.exception.InvalidOrderStateException;
import application.domain.valueobject.TrackingNumber;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * The logistics process associated with the physical fulfillment of an order.
 * Physical products require logistics processing; dispatch occurs after the
 * required order conditions are satisfied.
 */
@Getter
public class Shipment {

    private Long id;
    private Long orderId;
    private Long warehouseId;
    private TrackingNumber trackingNumber;
    private ShipmentStatus status;
    private LocalDateTime shipmentDate;
    private LocalDateTime deliveryDate;

    private Shipment() {
    }

    public static Shipment create(Long orderId, Long warehouseId, TrackingNumber trackingNumber) {
        if (orderId == null) {
            throw new IllegalArgumentException("OrderId must not be null");
        }
        if (warehouseId == null) {
            throw new IllegalArgumentException("WarehouseId must not be null");
        }
        if (trackingNumber == null) {
            throw new IllegalArgumentException("Tracking number must not be null");
        }
        Shipment shipment = new Shipment();
        shipment.orderId = orderId;
        shipment.warehouseId = warehouseId;
        shipment.trackingNumber = trackingNumber;
        shipment.status = ShipmentStatus.PREPARING;
        return shipment;
    }

    public void prepare() {
        requireStatus(ShipmentStatus.PREPARING, "prepare");
    }

    public void dispatch() {
        requireStatus(ShipmentStatus.PREPARING, "dispatch");
        this.status = ShipmentStatus.DISPATCHED;
        this.shipmentDate = LocalDateTime.now();
    }

    public void markInTransit() {
        requireStatus(ShipmentStatus.DISPATCHED, "mark in transit");
        this.status = ShipmentStatus.IN_TRANSIT;
    }

    public void markDelivered() {
        requireStatus(ShipmentStatus.IN_TRANSIT, "mark delivered");
        this.status = ShipmentStatus.DELIVERED;
        this.deliveryDate = LocalDateTime.now();
    }

    private void requireStatus(ShipmentStatus expected, String operation) {
        if (status != expected) {
            throw new InvalidOrderStateException(
                    "Cannot " + operation + " a shipment in status " + status);
        }
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}