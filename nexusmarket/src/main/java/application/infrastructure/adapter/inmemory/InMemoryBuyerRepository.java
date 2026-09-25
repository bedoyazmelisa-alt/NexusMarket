package application.infrastructure.adapter.inmemory;

import application.domain.model.Buyer;
import application.domain.port.out.BuyerRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * In-memory {@link BuyerRepository}. Stand-in until the MySQL adapter exists.
 */
@Repository
public class InMemoryBuyerRepository extends InMemoryRepository<Buyer> implements BuyerRepository {

    public InMemoryBuyerRepository() {
        super(Buyer::getId, Buyer::assignId);
    }

    @Override
    public Buyer save(Buyer buyer) {
        return persist(buyer);
    }

    @Override
    public Optional<Buyer> findById(Long id) {
        return row(id);
    }

    @Override
    public Optional<Buyer> findByUserId(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }
        return all().stream().filter(buyer -> userId.equals(buyer.getUserId())).findFirst();
    }
}
