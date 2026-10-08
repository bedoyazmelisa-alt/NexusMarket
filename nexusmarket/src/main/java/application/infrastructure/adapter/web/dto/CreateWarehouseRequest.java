package application.infrastructure.adapter.web.dto;

import application.domain.enums.WarehouseType;

/** Request to create a warehouse. */
public record CreateWarehouseRequest(String name, AddressRequest address, WarehouseType warehouseType) {
}
