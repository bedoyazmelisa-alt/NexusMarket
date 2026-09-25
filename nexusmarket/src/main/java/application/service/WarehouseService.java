package application.service;

import application.domain.enums.WarehouseType;
import application.domain.model.Warehouse;
import application.domain.port.in.CreateWarehouseUseCase;
import application.domain.port.out.WarehouseRepository;
import application.domain.valueobject.Address;

/**
 * Application service for warehouse management. Implements
 * {@link CreateWarehouseUseCase}: a warehouse must have a valid type and a
 * valid address.
 */
public class WarehouseService implements CreateWarehouseUseCase {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Warehouse createWarehouse(String name, Address address, WarehouseType warehouseType) {
        return warehouseRepository.save(Warehouse.create(name, address, warehouseType));
    }
}
