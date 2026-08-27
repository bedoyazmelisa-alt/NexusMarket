package application.domain.port.in;

import application.domain.model.Cart;

/**
 * Input port: creates a provisional cart for a buyer.
 */
public interface CreateCartUseCase {

    Cart createCart(Long buyerId);
}