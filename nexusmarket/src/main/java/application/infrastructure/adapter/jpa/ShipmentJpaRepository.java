package application.infrastructure.adapter.jpa;

import application.infrastructure.adapter.jpa.entity.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for {@link ShipmentEntity}. Technical query layer only;
 * domain logic never sees this interface.
 */
public interface ShipmentJpaRepository extends JpaRepository<ShipmentEntity, Long> {

    List<ShipmentEntity> findByOrderId(Long orderId);
}
