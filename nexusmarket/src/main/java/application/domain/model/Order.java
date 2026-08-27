package application.domain.model;

import application.domain.enums.OrderStatus;
import application.domain.exception.FinalizedOrderModificationException;
import application.domain.exception.InvalidOrderStateException;
import application.domain.valueobject.Money;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The formal commercial commitment created by a buyer. Payment must be
 * confirmed before fulfillment (RG-11), the order must follow valid state
 * transitions (RG-10), and a finalized order cannot be modified (RG-12).
 */
@Getter
public class Order {

    private Long id;
    private Long buyerId;
    private OrderStatus status;
    private Money totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final List<OrderItem> items = new ArrayList<>();

    private Order() {
    }

    public static Order create(Long buyerId) {
        if (buyerId == null) {
            throw new IllegalArgumentException("BuyerId must not be null");
        }
        Order order = new Order();
        order.buyerId = buyerId;
        order.status = OrderStatus.PENDING_PAYMENT;
        order.createdAt = LocalDateTime.now();
        order.updatedAt = order.createdAt;
        return order;
    }

    public OrderItem addItem(Long productId, int quantity, Money unitPrice) {
        ensureModifiable();
        OrderItem item = OrderItem.create(id, productId, quantity, unitPrice);
        items.add(item);
        touch();
        return item;
    }

    public void removeItem(Long productId) {
        ensureModifiable();
        if (!items.removeIf(item -> item.getProductId().equals(productId))) {
            throw new IllegalArgumentException("Product is not in the order");
        }
        touch();
    }

    public void calculateTotal() {
        ensureModifiable();
        if (items.isEmpty()) {
            throw new InvalidOrderStateException("Cannot calculate total for an order without items");
        }
        Money total = items.get(0).getSubtotal();
        for (int i = 1; i < items.size(); i++) {
            total = total.add(items.get(i).getSubtotal());
        }
        this.totalAmount = total;
        touch();
    }

    public void markAsPendingPayment() {
        requireStatus(OrderStatus.PENDING_PAYMENT, "mark as pending payment");
        this.status = OrderStatus.PENDING_PAYMENT;
        touch();
    }

    public void markAsPaid() {
        requireStatus(OrderStatus.PENDING_PAYMENT, "mark as paid");
        this.status = OrderStatus.PAID;
        touch();
    }

    public void markAsDispatched() {
        requireStatus(OrderStatus.PAID, "mark as dispatched");
        this.status = OrderStatus.DISPATCHED;
        touch();
    }

    public void markAsDelivered() {
        requireStatus(OrderStatus.DISPATCHED, "mark as delivered");
        this.status = OrderStatus.DELIVERED;
        touch();
    }

    public void finalize() {
        requireStatus(OrderStatus.DELIVERED, "finalize");
        this.status = OrderStatus.FINALIZED;
        touch();
    }

    public boolean isFinalized() {
        return status == OrderStatus.FINALIZED;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    private void requireStatus(OrderStatus expected, String operation) {
        if (status != expected) {
            throw new InvalidOrderStateException(
                    "Cannot " + operation + " an order in status " + status);
        }
    }

    private void ensureModifiable() {
        if (isFinalized()) {
            throw new FinalizedOrderModificationException(
                    "A finalized order cannot be modified");
        }
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}