package application.domain.port.out;

import application.domain.model.Cart;

import java.util.Optional;

/**
 * Output port for persisting and retrieving {@link Cart} aggregates.
 */
public interface CartRepository {

    Cart save(Cart cart);

    Optional<Cart> findById(Long id);

    Optional<Cart> findOpenCartByBuyerId(Long buyerId);
}