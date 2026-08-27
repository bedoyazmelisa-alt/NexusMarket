package application.domain.port.out;

import application.domain.model.Shipment;

import java.util.List;
import java.util.Optional;

/**
 * Output port for persisting and retrieving {@link Shipment} entities.
 */
public interface ShipmentRepository {

    Shipment save(Shipment shipment);

    Optional<Shipment> findById(Long id);

    List<Shipment> findByOrderId(Long orderId);
}