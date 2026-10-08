package application.infrastructure.adapter.jpa;

import application.infrastructure.adapter.jpa.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for {@link InventoryEntity}. Technical query layer only;
 * domain logic never sees this interface.
 */
public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    List<InventoryEntity> findByProductId(Long productId);
}
