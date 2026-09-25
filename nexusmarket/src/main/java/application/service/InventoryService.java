package application.service;

import application.domain.exception.InvalidInventoryMovementException;
import application.domain.model.Inventory;
import application.domain.port.in.ManageInventoryUseCase;
import application.domain.port.out.InventoryRepository;
import application.domain.port.out.ProductRepository;
import application.domain.port.out.WarehouseRepository;

/**
 * Application service for inventory stock operations. Implements
 * {@link ManageInventoryUseCase} (entry, reservation, sale, adjustment,
 * return).
 *
 * <p>Every stock change goes through domain behavior, so inventory can never
 * become negative (RG-07), damaged stock cannot be reserved (RG-09) and every
 * movement is recorded. An initial entry also validates that the product and
 * warehouse exist (RG-06).</p>
 */
public class InventoryService implements ManageInventoryUseCase {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    public InventoryService(InventoryRepository inventoryRepository,
                            ProductRepository productRepository,
                            WarehouseRepository warehouseRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Inventory registerEntry(Long productId, Long warehouseId, int quantity, String reason) {
        if (productId == null || warehouseId == null) {
            throw new InvalidInventoryMovementException(
                    "Inventory must be associated with a product and a warehouse (RG-06)");
        }
        productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product does not exist"));
        warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse does not exist"));

        Inventory inventory = inventoryRepository.findByProductAndWarehouse(productId, warehouseId)
                .orElseGet(() -> Inventory.create(productId, warehouseId, 0));
        inventory.increase(quantity, reason);
        return inventoryRepository.save(inventory);
    }

    @Override
    public void reserve(Long productId, Long warehouseId, int quantity, String reason) {
        Inventory inventory = requireInventory(productId, warehouseId);
        inventory.reserve(quantity, reason);
        inventoryRepository.save(inventory);
    }

    @Override
    public void releaseReservation(Long productId, Long warehouseId, int quantity, String reason) {
        Inventory inventory = requireInventory(productId, warehouseId);
        inventory.releaseReservation(quantity, reason);
        inventoryRepository.save(inventory);
    }

    @Override
    public void registerSale(Long productId, Long warehouseId, int quantity, String reason) {
        Inventory inventory = requireInventory(productId, warehouseId);
        inventory.registerSale(quantity, reason);
        inventoryRepository.save(inventory);
    }

    @Override
    public void adjust(Long productId, Long warehouseId, int newAvailableQuantity, String reason) {
        Inventory inventory = requireInventory(productId, warehouseId);
        inventory.adjust(newAvailableQuantity, reason);
        inventoryRepository.save(inventory);
    }

    @Override
    public void registerReturn(Long productId, Long warehouseId, int quantity, String reason) {
        Inventory inventory = requireInventory(productId, warehouseId);
        inventory.registerReturn(quantity, reason);
        inventoryRepository.save(inventory);
    }

    private Inventory requireInventory(Long productId, Long warehouseId) {
        if (productId == null || warehouseId == null) {
            throw new InvalidInventoryMovementException(
                    "Inventory must be associated with a product and a warehouse (RG-06)");
        }
        return inventoryRepository.findByProductAndWarehouse(productId, warehouseId)
                .orElseThrow(() -> new InvalidInventoryMovementException(
                        "No inventory for product " + productId + " in warehouse " + warehouseId));
    }
}
