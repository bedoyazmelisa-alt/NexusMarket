package application.domain.port.in;

import application.domain.model.Order;

/**
 * Input port: creates a formal order from a cart, validating the buyer,
 * the cart, product availability and reserving inventory.
 */
public interface CreateOrderUseCase {

    Order createOrder(Long cartId);
}