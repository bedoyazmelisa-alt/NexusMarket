package application.infrastructure.adapter.jpa;

import application.domain.model.Inventory;
import application.domain.model.InventoryMovement;
import application.domain.port.out.InventoryRepository;
import application.infrastructure.adapter.jpa.entity.InventoryEntity;
import application.infrastructure.adapter.jpa.entity.InventoryMovementEmbeddable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MySQL/JPA implementation of {@link InventoryRepository}. Translates between the
 * persistence model ({@link InventoryEntity}) and the domain model; no business
 * rules live here.
 */
@Repository
@Profile("!in-memory")
public class JpaInventoryRepository implements InventoryRepository {

    private final InventoryJpaRepository jpaRepository;

    public JpaInventoryRepository(InventoryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Inventory save(Inventory inventory) {
        InventoryEntity entity = toEntity(inventory);
        InventoryEntity saved = jpaRepository.save(entity);
        if (inventory.getId() == null) {
            inventory.assignId(saved.getId());
        }
        return inventory;
    }

    @Override
    public Optional<Inventory> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Inventory> findByProductAndWarehouse(Long productId, Long warehouseId) {
        if (productId == null || warehouseId == null) {
            return Optional.empty();
        }
        return jpaRepository.findByProductIdAndWarehouseId(productId, warehouseId).map(this::toDomain);
    }

    @Override
    public List<Inventory> findByProductId(Long productId) {
        if (productId == null) {
            return List.of();
        }
        return jpaRepository.findByProductId(productId).stream().map(this::toDomain).toList();
    }

    private InventoryEntity toEntity(Inventory inventory) {
        InventoryEntity entity = new InventoryEntity();
        entity.setId(inventory.getId());
        entity.setProductId(inventory.getProductId());
        entity.setWarehouseId(inventory.getWarehouseId());
        entity.setAvailableQuantity(inventory.getAvailableQuantity());
        entity.setReservedQuantity(inventory.getReservedQuantity());
        entity.setDamagedQuantity(inventory.getDamagedQuantity());
        entity.setMovements(inventory.getMovements().stream().map(this::toEmbeddable).toList());
        return entity;
    }

    private Inventory toDomain(InventoryEntity entity) {
        List<InventoryMovement> movements = entity.getMovements() == null
                ? List.of()
                : entity.getMovements().stream()
                        .map(embeddable -> toMovementDomain(embeddable, entity.getId()))
                        .toList();
        return Inventory.reconstitute(
                entity.getId(),
                entity.getProductId(),
                entity.getWarehouseId(),
                entity.getAvailableQuantity(),
                entity.getReservedQuantity(),
                entity.getDamagedQuantity(),
                movements);
    }

    private InventoryMovementEmbeddable toEmbeddable(InventoryMovement movement) {
        InventoryMovementEmbeddable embeddable = new InventoryMovementEmbeddable();
        embeddable.setId(movement.getId());
        embeddable.setMovementType(movement.getMovementType());
        embeddable.setQuantity(movement.getQuantity());
        embeddable.setDate(movement.getDate());
        embeddable.setReason(movement.getReason());
        return embeddable;
    }

    private InventoryMovement toMovementDomain(InventoryMovementEmbeddable embeddable, Long inventoryId) {
        return InventoryMovement.reconstitute(
                embeddable.getId(),
                inventoryId,
                embeddable.getMovementType(),
                embeddable.getQuantity(),
                embeddable.getDate(),
                embeddable.getReason());
    }
}
