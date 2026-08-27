package application.domain.port.in;

import application.domain.enums.WarehouseType;
import application.domain.model.Warehouse;
import application.domain.valueobject.Address;

/**
 * Input port: creates a warehouse of a valid type.
 */
public interface CreateWarehouseUseCase {

    Warehouse createWarehouse(String name, Address address, WarehouseType warehouseType);
}