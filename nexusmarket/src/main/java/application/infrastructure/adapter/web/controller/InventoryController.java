package application.infrastructure.adapter.web.controller;

import application.domain.model.Inventory;
import application.domain.port.in.ManageInventoryUseCase;
import application.infrastructure.adapter.web.dto.InventoryAdjustmentRequest;
import application.infrastructure.adapter.web.dto.InventoryRequest;
import application.infrastructure.adapter.web.dto.InventoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes {@link ManageInventoryUseCase} over HTTP.
 *
 * <p>Endpoints: {@code POST /api/inventory/entries} (201 + body),
 * {@code POST /api/inventory/reservations} (204),
 * {@code POST /api/inventory/reservations/release} (204),
 * {@code POST /api/inventory/sales} (204),
 * {@code POST /api/inventory/adjustments} (204),
 * {@code POST /api/inventory/returns} (204).</p>
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final ManageInventoryUseCase manageInventory;

    public InventoryController(ManageInventoryUseCase manageInventory) {
        this.manageInventory = manageInventory;
    }

    @PostMapping("/entries")
    public ResponseEntity<InventoryResponse> registerEntry(@RequestBody InventoryRequest request) {
        Inventory inventory = manageInventory.registerEntry(request.productId(), request.warehouseId(),
                request.quantity(), request.reason());
        return ResponseEntity.status(HttpStatus.CREATED).body(InventoryResponse.from(inventory));
    }

    @PostMapping("/reservations")
    public ResponseEntity<Void> reserve(@RequestBody InventoryRequest request) {
        manageInventory.reserve(request.productId(), request.warehouseId(),
                request.quantity(), request.reason());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reservations/release")
    public ResponseEntity<Void> releaseReservation(@RequestBody InventoryRequest request) {
        manageInventory.releaseReservation(request.productId(), request.warehouseId(),
                request.quantity(), request.reason());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sales")
    public ResponseEntity<Void> registerSale(@RequestBody InventoryRequest request) {
        manageInventory.registerSale(request.productId(), request.warehouseId(),
                request.quantity(), request.reason());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/adjustments")
    public ResponseEntity<Void> adjust(@RequestBody InventoryAdjustmentRequest request) {
        manageInventory.adjust(request.productId(), request.warehouseId(),
                request.newAvailableQuantity(), request.reason());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/returns")
    public ResponseEntity<Void> registerReturn(@RequestBody InventoryRequest request) {
        manageInventory.registerReturn(request.productId(), request.warehouseId(),
                request.quantity(), request.reason());
        return ResponseEntity.noContent().build();
    }
}
