package application.infrastructure.adapter.web.dto;

import application.domain.enums.OrderStatus;
import application.domain.model.Order;

/** Order view returned by the API (primitives only — no domain types). */
public record OrderResponse(Long id, Long buyerId, OrderStatus status, MoneyResponse totalAmount) {

    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(), order.getBuyerId(), order.getStatus(),
                MoneyResponse.from(order.getTotalAmount()));
    }
}
