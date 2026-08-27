package application.domain.port.out;

import application.domain.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * Output port for persisting and retrieving {@link Order} aggregates.
 */
public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findByBuyerId(Long buyerId);
}