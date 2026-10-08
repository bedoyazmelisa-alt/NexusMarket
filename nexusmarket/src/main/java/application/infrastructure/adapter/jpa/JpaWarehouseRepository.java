package application.infrastructure.adapter.jpa;

import application.domain.model.Warehouse;
import application.domain.port.out.WarehouseRepository;
import application.domain.valueobject.Address;
import application.infrastructure.adapter.jpa.entity.WarehouseEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MySQL/JPA implementation of {@link WarehouseRepository}. Translates between
 * the persistence model ({@link WarehouseEntity}) and the domain model; no
 * business rules live here.
 */
@Repository
@Profile("!in-memory")
public class JpaWarehouseRepository implements WarehouseRepository {

    private final WarehouseJpaRepository jpaRepository;

    public JpaWarehouseRepository(WarehouseJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Warehouse save(Warehouse warehouse) {
        WarehouseEntity entity = toEntity(warehouse);
        WarehouseEntity saved = jpaRepository.save(entity);
        if (warehouse.getId() == null) {
            warehouse.assignId(saved.getId());
        }
        return warehouse;
    }

    @Override
    public Optional<Warehouse> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Warehouse> findBySeller(Long sellerId) {
        // TODO: the Seller -> Warehouse association is documented but not modeled
        // on the Warehouse entity yet, so no warehouse can be matched today.
        return List.of();
    }

    private WarehouseEntity toEntity(Warehouse warehouse) {
        WarehouseEntity entity = new WarehouseEntity();
        entity.setId(warehouse.getId());
        entity.setName(warehouse.getName());
        entity.setStreet(warehouse.getAddress().getStreet());
        entity.setCity(warehouse.getAddress().getCity());
        entity.setState(warehouse.getAddress().getState());
        entity.setPostalCode(warehouse.getAddress().getPostalCode());
        entity.setCountry(warehouse.getAddress().getCountry());
        entity.setWarehouseType(warehouse.getWarehouseType());
        entity.setStatus(warehouse.getStatus());
        return entity;
    }

    private Warehouse toDomain(WarehouseEntity entity) {
        return Warehouse.reconstitute(
                entity.getId(),
                entity.getName(),
                Address.of(entity.getStreet(), entity.getCity(), entity.getState(),
                        entity.getPostalCode(), entity.getCountry()),
                entity.getWarehouseType(),
                entity.getStatus());
    }
}
