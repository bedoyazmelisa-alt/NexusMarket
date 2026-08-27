package application.domain.model;

import application.domain.valueobject.Money;
import lombok.Getter;

/**
 * A product included in an order. Quantity must be greater than zero, unit
 * price must be valid and the subtotal must stay consistent with quantity
 * and unit price.
 */
@Getter
public class OrderItem {

    private Long id;
    private Long orderId;
    private Long productId;
    private int quantity;
    private Money unitPrice;
    private Money subtotal;

    private OrderItem() {
    }

    public static OrderItem create(Long orderId, Long productId, int quantity, Money unitPrice) {
        if (productId == null) {
            throw new IllegalArgumentException("ProductId must not be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("Unit price must not be null");
        }
        OrderItem item = new OrderItem();
        item.orderId = orderId;
        item.productId = productId;
        item.quantity = quantity;
        item.unitPrice = unitPrice;
        item.subtotal = unitPrice.multiply(quantity);
        return item;
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}