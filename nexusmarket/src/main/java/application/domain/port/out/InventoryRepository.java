package application.domain.port.out;

import application.domain.model.Inventory;

import java.util.List;
import java.util.Optional;

/**
 * Output port for persisting and retrieving {@link Inventory} aggregates.
 */
public interface InventoryRepository {

    Inventory save(Inventory inventory);

    Optional<Inventory> findById(Long id);

    Optional<Inventory> findByProductAndWarehouse(Long productId, Long warehouseId);

    List<Inventory> findByProductId(Long productId);
}