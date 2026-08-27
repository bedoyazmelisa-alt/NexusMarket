package application.domain.port.out;

import application.domain.model.Warehouse;

import java.util.List;
import java.util.Optional;

/**
 * Output port for persisting and retrieving {@link Warehouse} aggregates.
 */
public interface WarehouseRepository {

    Warehouse save(Warehouse warehouse);

    Optional<Warehouse> findById(Long id);

    List<Warehouse> findBySeller(Long sellerId);
}