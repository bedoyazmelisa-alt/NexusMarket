package application.infrastructure.adapter.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Persistence row for one {@code OrderItem} line of an order. The subtotal is
 * derived state and is recomputed by the domain on rehydration.
 */
@Embeddable
@Getter
@Setter
public class OrderItemEmbeddable {

    @Column(name = "id")
    private Long id;

    // Read-only: shares the collection's order_id foreign key; a writable copy of the column would collide with it.
    @Column(name = "order_id", insertable = false, updatable = false)
    private Long orderId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_price_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPriceAmount;

    @Column(name = "unit_price_currency", nullable = false, length = 3)
    private String unitPriceCurrency;
}
