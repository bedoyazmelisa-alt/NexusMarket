package application.infrastructure.adapter.web.dto;

/** Request to set the available quantity of a product in a warehouse. */
public record InventoryAdjustmentRequest(Long productId, Long warehouseId,
                                         int newAvailableQuantity, String reason) {
}
