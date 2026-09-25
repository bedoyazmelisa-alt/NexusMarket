package application.infrastructure.adapter.inmemory;

import application.domain.model.Warehouse;
import application.domain.port.out.WarehouseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * In-memory {@link WarehouseRepository}. Stand-in until the MySQL adapter
 * exists.
 */
@Repository
public class InMemoryWarehouseRepository extends InMemoryRepository<Warehouse> implements WarehouseRepository {

    public InMemoryWarehouseRepository() {
        super(Warehouse::getId, Warehouse::assignId);
    }

    @Override
    public Warehouse save(Warehouse warehouse) {
        return persist(warehouse);
    }

    @Override
    public Optional<Warehouse> findById(Long id) {
        return row(id);
    }

    @Override
    public List<Warehouse> findBySeller(Long sellerId) {
        // TODO: the Seller -> Warehouse association is documented but not modeled
        // on the Warehouse entity yet, so no warehouse can be matched today.
        return List.of();
    }
}
