package application.infrastructure.adapter.web.dto;

/** Request for a quantity-based inventory operation (entry, reservation, release, sale, return). */
public record InventoryRequest(Long productId, Long warehouseId, int quantity, String reason) {
}
