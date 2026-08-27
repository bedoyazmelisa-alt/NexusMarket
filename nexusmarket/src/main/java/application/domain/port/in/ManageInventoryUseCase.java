package application.domain.port.in;

import application.domain.model.Inventory;

/**
 * Input port: manages inventory stock operations (entry, reservation,
 * sale, adjustment, return, damage).
 */
public interface ManageInventoryUseCase {

    Inventory registerEntry(Long productId, Long warehouseId, int quantity, String reason);

    void reserve(Long productId, Long warehouseId, int quantity, String reason);

    void releaseReservation(Long productId, Long warehouseId, int quantity, String reason);

    void registerSale(Long productId, Long warehouseId, int quantity, String reason);

    void adjust(Long productId, Long warehouseId, int newAvailableQuantity, String reason);

    void registerReturn(Long productId, Long warehouseId, int quantity, String reason);
}