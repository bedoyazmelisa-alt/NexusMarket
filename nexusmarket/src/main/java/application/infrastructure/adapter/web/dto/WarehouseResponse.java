package application.infrastructure.adapter.web.dto;

import application.domain.enums.WarehouseStatus;
import application.domain.enums.WarehouseType;
import application.domain.model.Warehouse;

/** Warehouse view returned by the API (primitives only — no domain types). */
public record WarehouseResponse(Long id, String name, AddressResponse address,
                                WarehouseType warehouseType, WarehouseStatus status) {

    public static WarehouseResponse from(Warehouse warehouse) {
        return new WarehouseResponse(warehouse.getId(), warehouse.getName(),
                AddressResponse.from(warehouse.getAddress()),
                warehouse.getWarehouseType(), warehouse.getStatus());
    }
}
