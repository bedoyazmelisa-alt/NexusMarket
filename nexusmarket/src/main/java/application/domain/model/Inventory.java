package application.domain.model;

import application.domain.enums.InventoryMovementType;
import application.domain.exception.DamagedInventoryException;
import application.domain.exception.InsufficientInventoryException;
import application.domain.exception.InvalidInventoryMovementException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Available stock of a product within a specific warehouse. Inventory is
 * distributed and must always be associated with both a product and a
 * warehouse (RG-06). Negative inventory is never allowed (RG-07).
 */
@Getter
public class Inventory {

    private Long id;
    private Long productId;
    private Long warehouseId;
    private int availableQuantity;
    private int reservedQuantity;
    private int damagedQuantity;
    private final List<InventoryMovement> movements = new ArrayList<>();

    private Inventory() {
    }

    public static Inventory create(Long productId, Long warehouseId, int initialQuantity) {
        if (productId == null) {
            throw new IllegalArgumentException("ProductId must not be null");
        }
        if (warehouseId == null) {
            throw new IllegalArgumentException("WarehouseId must not be null");
        }
        if (initialQuantity < 0) {
            throw new InvalidInventoryMovementException("Initial quantity must not be negative");
        }
        Inventory inventory = new Inventory();
        inventory.productId = productId;
        inventory.warehouseId = warehouseId;
        inventory.availableQuantity = initialQuantity;
        return inventory;
    }

    public void increase(int quantity, String reason) {
        if (quantity <= 0) {
            throw new InvalidInventoryMovementException("Quantity must be positive");
        }
        this.availableQuantity += quantity;
        recordMovement(InventoryMovementType.ENTRY, quantity, reason);
    }

    public void reserve(int quantity, String reason) {
        if (quantity <= 0) {
            throw new InvalidInventoryMovementException("Reservation quantity must be positive");
        }
        if (quantity > availableQuantity) {
            if (damagedQuantity > 0 && availableQuantity == 0) {
                throw new DamagedInventoryException(
                        "Cannot reserve: remaining stock is damaged");
            }
            throw new InsufficientInventoryException(
                    "Insufficient available inventory: requested " + quantity
                            + ", available " + availableQuantity);
        }
        this.availableQuantity -= quantity;
        this.reservedQuantity += quantity;
        recordMovement(InventoryMovementType.RESERVATION, quantity, reason);
    }

    public void releaseReservation(int quantity, String reason) {
        if (quantity <= 0) {
            throw new InvalidInventoryMovementException("Release quantity must be positive");
        }
        if (quantity > reservedQuantity) {
            throw new InvalidInventoryMovementException(
                    "Cannot release " + quantity + " units: only " + reservedQuantity + " reserved");
        }
        this.reservedQuantity -= quantity;
        this.availableQuantity += quantity;
        recordMovement(InventoryMovementType.ADJUSTMENT, quantity, reason);
    }

    public void registerSale(int quantity, String reason) {
        if (quantity <= 0) {
            throw new InvalidInventoryMovementException("Sale quantity must be positive");
        }
        if (quantity > reservedQuantity) {
            throw new InvalidInventoryMovementException(
                    "Cannot sell " + quantity + " units: only " + reservedQuantity + " reserved");
        }
        this.reservedQuantity -= quantity;
        recordMovement(InventoryMovementType.SALE, quantity, reason);
    }

    public void adjust(int newAvailableQuantity, String reason) {
        if (newAvailableQuantity < 0) {
            throw new InvalidInventoryMovementException("Adjusted quantity must not be negative");
        }
        this.availableQuantity = newAvailableQuantity;
        recordMovement(InventoryMovementType.ADJUSTMENT, newAvailableQuantity, reason);
    }

    public void registerReturn(int quantity, String reason) {
        if (quantity <= 0) {
            throw new InvalidInventoryMovementException("Return quantity must be positive");
        }
        this.availableQuantity += quantity;
        recordMovement(InventoryMovementType.RETURN, quantity, reason);
    }

    public void registerDamage(int quantity, String reason) {
        if (quantity <= 0) {
            throw new InvalidInventoryMovementException("Damage quantity must be positive");
        }
        if (quantity > availableQuantity) {
            throw new InvalidInventoryMovementException(
                    "Cannot mark " + quantity + " units as damaged: only " + availableQuantity + " available");
        }
        this.availableQuantity -= quantity;
        this.damagedQuantity += quantity;
        recordMovement(InventoryMovementType.ADJUSTMENT, quantity, reason);
    }

    public int getTotalQuantity() {
        return availableQuantity + reservedQuantity + damagedQuantity;
    }

    public List<InventoryMovement> getMovements() {
        return Collections.unmodifiableList(movements);
    }

    private void recordMovement(InventoryMovementType type, int quantity, String reason) {
        movements.add(InventoryMovement.create(id, type, quantity, reason));
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}