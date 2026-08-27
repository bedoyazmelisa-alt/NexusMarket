package application.domain.service;

import application.domain.exception.InvalidInventoryMovementException;
import application.domain.model.Inventory;
import application.domain.port.out.InventoryRepository;

/**
 * Domain service that coordinates inventory reservations. Enforces:
 * inventory must exist, must not be damaged, requested quantity must be
 * available, and inventory cannot become negative.
 */
public class InventoryReservationService {

    private final InventoryRepository inventoryRepository;

    public InventoryReservationService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void reserve(Long inventoryId, int quantity, String reason) {
        Inventory inventory = requireInventory(inventoryId);
        inventory.reserve(quantity, reason);
        inventoryRepository.save(inventory);
    }

    public void releaseReservation(Long inventoryId, int quantity, String reason) {
        Inventory inventory = requireInventory(inventoryId);
        inventory.releaseReservation(quantity, reason);
        inventoryRepository.save(inventory);
    }

    private Inventory requireInventory(Long inventoryId) {
        if (inventoryId == null) {
            throw new InvalidInventoryMovementException("Inventory must exist");
        }
        return inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InvalidInventoryMovementException(
                        "Inventory with id " + inventoryId + " does not exist"));
    }
}