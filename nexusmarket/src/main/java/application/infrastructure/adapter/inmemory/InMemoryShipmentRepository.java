package application.infrastructure.adapter.inmemory;

import application.domain.model.Shipment;
import application.domain.port.out.ShipmentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * In-memory {@link ShipmentRepository}. Stand-in until the MySQL adapter
 * exists.
 */
@Repository
public class InMemoryShipmentRepository extends InMemoryRepository<Shipment> implements ShipmentRepository {

    public InMemoryShipmentRepository() {
        super(Shipment::getId, Shipment::assignId);
    }

    @Override
    public Shipment save(Shipment shipment) {
        return persist(shipment);
    }

    @Override
    public Optional<Shipment> findById(Long id) {
        return row(id);
    }

    @Override
    public List<Shipment> findByOrderId(Long orderId) {
        if (orderId == null) {
            return List.of();
        }
        return all().stream().filter(shipment -> orderId.equals(shipment.getOrderId())).toList();
    }
}
