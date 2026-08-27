package application.domain.model;

import application.domain.enums.InventoryMovementType;
import application.domain.exception.InvalidInventoryMovementException;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * A business event that changes inventory. Movement quantity must be valid
 * and movements must never result in negative inventory.
 */
@Getter
public class InventoryMovement {

    private Long id;
    private Long inventoryId;
    private InventoryMovementType movementType;
    private int quantity;
    private LocalDateTime date;
    private String reason;

    private InventoryMovement() {
    }

    public static InventoryMovement create(Long inventoryId, InventoryMovementType movementType,
                                           int quantity, String reason) {
        if (movementType == null) {
            throw new InvalidInventoryMovementException("Movement type must not be null");
        }
        if (quantity <= 0) {
            throw new InvalidInventoryMovementException("Movement quantity must be positive");
        }
        InventoryMovement movement = new InventoryMovement();
        movement.inventoryId = inventoryId;
        movement.movementType = movementType;
        movement.quantity = quantity;
        movement.date = LocalDateTime.now();
        movement.reason = reason;
        return movement;
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}