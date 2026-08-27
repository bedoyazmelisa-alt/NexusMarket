package application.domain.service;

import application.domain.enums.OrderStatus;
import application.domain.exception.InvalidOrderStateException;
import application.domain.model.Order;
import application.domain.valueobject.Money;

import java.util.EnumMap;
import java.util.Map;

/**
 * Domain service that validates order state transitions and order
 * consistency (RG-10).
 */
public class OrderValidationService {

    private static final Map<OrderStatus, OrderStatus> VALID_TRANSITIONS = new EnumMap<>(OrderStatus.class);

    static {
        VALID_TRANSITIONS.put(OrderStatus.PENDING_PAYMENT, OrderStatus.PAID);
        VALID_TRANSITIONS.put(OrderStatus.PAID, OrderStatus.DISPATCHED);
        VALID_TRANSITIONS.put(OrderStatus.DISPATCHED, OrderStatus.DELIVERED);
        VALID_TRANSITIONS.put(OrderStatus.DELIVERED, OrderStatus.FINALIZED);
    }

    public boolean isValidTransition(OrderStatus from, OrderStatus to) {
        return from != null && to != null
                && VALID_TRANSITIONS.get(from) == to;
    }

    public void validate(Order order) {
        if (order == null) {
            throw new InvalidOrderStateException("Order must exist");
        }
        if (order.getItems().isEmpty()) {
            throw new InvalidOrderStateException("Order must contain at least one item");
        }
        if (order.getTotalAmount() == null) {
            throw new InvalidOrderStateException("Order total must be calculated");
        }
        Money expected = order.getItems().get(0).getSubtotal();
        for (int i = 1; i < order.getItems().size(); i++) {
            expected = expected.add(order.getItems().get(i).getSubtotal());
        }
        if (!expected.equals(order.getTotalAmount())) {
            throw new InvalidOrderStateException(
                    "Order total is inconsistent with its items");
        }
    }
}