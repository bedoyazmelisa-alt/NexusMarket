package application.domain.service;

import application.domain.exception.InvalidOrderStateException;
import application.domain.model.Order;
import application.domain.model.OrderItem;
import application.domain.port.out.OrderRepository;
import application.domain.valueobject.Money;

/**
 * Domain service that coordinates the final stage of an order. Enforces the
 * rule that a finalized order cannot be modified (RG-12).
 */
public class OrderFinalizationService {

    private final OrderRepository orderRepository;

    public OrderFinalizationService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order finalize(Long orderId) {
        Order order = requireOrder(orderId);
        order.finalize();
        return orderRepository.save(order);
    }

    public Order addItem(Long orderId, Long productId, int quantity, Money unitPrice) {
        Order order = requireOrder(orderId);
        order.addItem(productId, quantity, unitPrice);
        return orderRepository.save(order);
    }

    public Order removeItem(Long orderId, Long productId) {
        Order order = requireOrder(orderId);
        order.removeItem(productId);
        return orderRepository.save(order);
    }

    public Order calculateTotal(Long orderId) {
        Order order = requireOrder(orderId);
        order.calculateTotal();
        return orderRepository.save(order);
    }

    private Order requireOrder(Long orderId) {
        if (orderId == null) {
            throw new InvalidOrderStateException("Order must exist");
        }
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidOrderStateException(
                        "Order with id " + orderId + " does not exist"));
    }
}