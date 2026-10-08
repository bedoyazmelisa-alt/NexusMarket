package application.infrastructure.adapter.jpa;

import application.infrastructure.adapter.jpa.entity.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link WarehouseEntity}. Technical query layer
 * only; domain logic never sees this interface.
 */
public interface WarehouseJpaRepository extends JpaRepository<WarehouseEntity, Long> {
}
