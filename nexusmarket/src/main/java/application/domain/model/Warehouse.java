package application.domain.model;

import application.domain.enums.WarehouseStatus;
import application.domain.enums.WarehouseType;
import application.domain.valueobject.Address;
import lombok.Getter;

/**
 * A physical location where inventory is stored. NexusMarket distinguishes
 * Marketplace warehouses from Seller warehouses.
 */
@Getter
public class Warehouse {

    private Long id;
    private String name;
    private Address address;
    private WarehouseType warehouseType;
    private WarehouseStatus status;

    private Warehouse() {
    }

    public static Warehouse create(String name, Address address, WarehouseType warehouseType) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (address == null) {
            throw new IllegalArgumentException("Address must not be null");
        }
        if (warehouseType == null) {
            throw new IllegalArgumentException("Warehouse type must not be null");
        }
        Warehouse warehouse = new Warehouse();
        warehouse.name = name.trim();
        warehouse.address = address;
        warehouse.warehouseType = warehouseType;
        warehouse.status = WarehouseStatus.ACTIVE;
        return warehouse;
    }

    public void activate() {
        this.status = WarehouseStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = WarehouseStatus.INACTIVE;
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}