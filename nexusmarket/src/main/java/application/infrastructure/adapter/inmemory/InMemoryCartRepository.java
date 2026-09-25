package application.infrastructure.adapter.inmemory;

import application.domain.enums.CartStatus;
import application.domain.model.Cart;
import application.domain.port.out.CartRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * In-memory {@link CartRepository}. Stand-in until the MySQL adapter exists.
 */
@Repository
public class InMemoryCartRepository extends InMemoryRepository<Cart> implements CartRepository {

    public InMemoryCartRepository() {
        super(Cart::getId, Cart::assignId);
    }

    @Override
    public Cart save(Cart cart) {
        return persist(cart);
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return row(id);
    }

    @Override
    public Optional<Cart> findOpenCartByBuyerId(Long buyerId) {
        if (buyerId == null) {
            return Optional.empty();
        }
        return all().stream()
                .filter(cart -> buyerId.equals(cart.getBuyerId()))
                .filter(cart -> cart.getStatus() == CartStatus.OPEN)
                .findFirst();
    }
}
