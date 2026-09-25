package application.infrastructure.adapter.inmemory;

import application.domain.model.Inventory;
import application.domain.port.out.InventoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * In-memory {@link InventoryRepository}. Stand-in until the MySQL adapter
 * exists.
 */
@Repository
public class InMemoryInventoryRepository extends InMemoryRepository<Inventory> implements InventoryRepository {

    public InMemoryInventoryRepository() {
        super(Inventory::getId, Inventory::assignId);
    }

    @Override
    public Inventory save(Inventory inventory) {
        return persist(inventory);
    }

    @Override
    public Optional<Inventory> findById(Long id) {
        return row(id);
    }

    @Override
    public Optional<Inventory> findByProductAndWarehouse(Long productId, Long warehouseId) {
        if (productId == null || warehouseId == null) {
            return Optional.empty();
        }
        return all().stream()
                .filter(inventory -> productId.equals(inventory.getProductId()))
                .filter(inventory -> warehouseId.equals(inventory.getWarehouseId()))
                .findFirst();
    }

    @Override
    public List<Inventory> findByProductId(Long productId) {
        if (productId == null) {
            return List.of();
        }
        return all().stream()
                .filter(inventory -> productId.equals(inventory.getProductId()))
                .toList();
    }
}
