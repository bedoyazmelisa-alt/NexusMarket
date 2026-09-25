package application.infrastructure.adapter.inmemory;

import application.domain.model.Seller;
import application.domain.port.out.SellerRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * In-memory {@link SellerRepository}. Stand-in until the MySQL adapter exists.
 */
@Repository
public class InMemorySellerRepository extends InMemoryRepository<Seller> implements SellerRepository {

    public InMemorySellerRepository() {
        super(Seller::getId, Seller::assignId);
    }

    @Override
    public Seller save(Seller seller) {
        return persist(seller);
    }

    @Override
    public Optional<Seller> findById(Long id) {
        return row(id);
    }

    @Override
    public Optional<Seller> findByUserId(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }
        return all().stream().filter(seller -> userId.equals(seller.getUserId())).findFirst();
    }
}
