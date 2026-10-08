package application.infrastructure.adapter.web.controller;

import application.domain.model.Warehouse;
import application.domain.port.in.CreateWarehouseUseCase;
import application.domain.valueobject.Address;
import application.infrastructure.adapter.web.dto.CreateWarehouseRequest;
import application.infrastructure.adapter.web.dto.WarehouseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes {@link CreateWarehouseUseCase} over HTTP. */
@RestController
@RequestMapping("/api/warehouses")
public class WarehousesController {

    private final CreateWarehouseUseCase createWarehouse;

    public WarehousesController(CreateWarehouseUseCase createWarehouse) {
        this.createWarehouse = createWarehouse;
    }

    @PostMapping
    public ResponseEntity<WarehouseResponse> create(@RequestBody CreateWarehouseRequest request) {
        Address address = request.address() == null ? null : request.address().toDomain();
        Warehouse warehouse = createWarehouse.createWarehouse(request.name(), address,
                request.warehouseType());
        return ResponseEntity.status(HttpStatus.CREATED).body(WarehouseResponse.from(warehouse));
    }
}
