package application.domain.port.in;

import application.domain.model.Shipment;
import application.domain.valueobject.TrackingNumber;

/**
 * Input port: creates a shipment for the physical fulfillment of an order.
 */
public interface CreateShipmentUseCase {

    Shipment createShipment(Long orderId, Long warehouseId, TrackingNumber trackingNumber);
}