package application.infrastructure.adapter.jpa;

import application.domain.model.Shipment;
import application.domain.port.out.ShipmentRepository;
import application.domain.valueobject.TrackingNumber;
import application.infrastructure.adapter.jpa.entity.ShipmentEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MySQL/JPA implementation of {@link ShipmentRepository}. Translates between the
 * persistence model ({@link ShipmentEntity}) and the domain model; no business
 * rules live here.
 */
@Repository
@Profile("!in-memory")
public class JpaShipmentRepository implements ShipmentRepository {

    private final ShipmentJpaRepository jpaRepository;

    public JpaShipmentRepository(ShipmentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Shipment save(Shipment shipment) {
        ShipmentEntity entity = toEntity(shipment);
        ShipmentEntity saved = jpaRepository.save(entity);
        if (shipment.getId() == null) {
            shipment.assignId(saved.getId());
        }
        return shipment;
    }

    @Override
    public Optional<Shipment> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Shipment> findByOrderId(Long orderId) {
        if (orderId == null) {
            return List.of();
        }
        return jpaRepository.findByOrderId(orderId).stream().map(this::toDomain).toList();
    }

    private ShipmentEntity toEntity(Shipment shipment) {
        ShipmentEntity entity = new ShipmentEntity();
        entity.setId(shipment.getId());
        entity.setOrderId(shipment.getOrderId());
        entity.setWarehouseId(shipment.getWarehouseId());
        entity.setTrackingNumber(shipment.getTrackingNumber().getValue());
        entity.setStatus(shipment.getStatus());
        entity.setShipmentDate(shipment.getShipmentDate());
        entity.setDeliveryDate(shipment.getDeliveryDate());
        return entity;
    }

    private Shipment toDomain(ShipmentEntity entity) {
        return Shipment.reconstitute(
                entity.getId(),
                entity.getOrderId(),
                entity.getWarehouseId(),
                TrackingNumber.of(entity.getTrackingNumber()),
                entity.getStatus(),
                entity.getShipmentDate(),
                entity.getDeliveryDate());
    }
}
