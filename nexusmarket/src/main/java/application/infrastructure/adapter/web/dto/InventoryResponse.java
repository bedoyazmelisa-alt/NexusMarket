package application.infrastructure.adapter.web.dto;

import application.domain.model.Inventory;

/** Inventory view returned by the API (primitives only — no domain types). */
public record InventoryResponse(Long id, Long productId, Long warehouseId,
                                int availableQuantity, int reservedQuantity,
                                int damagedQuantity, int totalQuantity) {

    public static InventoryResponse from(Inventory inventory) {
        return new InventoryResponse(inventory.getId(), inventory.getProductId(),
                inventory.getWarehouseId(), inventory.getAvailableQuantity(),
                inventory.getReservedQuantity(), inventory.getDamagedQuantity(),
                inventory.getTotalQuantity());
    }
}
