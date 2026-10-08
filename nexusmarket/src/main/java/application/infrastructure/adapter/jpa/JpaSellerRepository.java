package application.infrastructure.adapter.jpa;

import application.domain.model.Seller;
import application.domain.port.out.SellerRepository;
import application.infrastructure.adapter.jpa.entity.SellerEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MySQL/JPA implementation of {@link SellerRepository}. Translates between the
 * persistence model ({@link SellerEntity}) and the domain model; no business
 * rules live here.
 */
@Repository
@Profile("!in-memory")
public class JpaSellerRepository implements SellerRepository {

    private final SellerJpaRepository jpaRepository;

    public JpaSellerRepository(SellerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Seller save(Seller seller) {
        SellerEntity entity = toEntity(seller);
        SellerEntity saved = jpaRepository.save(entity);
        if (seller.getId() == null) {
            seller.assignId(saved.getId());
        }
        return seller;
    }

    @Override
    public Optional<Seller> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Seller> findByUserId(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }
        return jpaRepository.findByUserId(userId).map(this::toDomain);
    }

    private SellerEntity toEntity(Seller seller) {
        SellerEntity entity = new SellerEntity();
        entity.setId(seller.getId());
        entity.setUserId(seller.getUserId());
        entity.setBusinessInformation(seller.getBusinessInformation());
        entity.setStatus(seller.getStatus());
        return entity;
    }

    private Seller toDomain(SellerEntity entity) {
        return Seller.reconstitute(
                entity.getId(),
                entity.getUserId(),
                entity.getBusinessInformation(),
                entity.getStatus());
    }
}
