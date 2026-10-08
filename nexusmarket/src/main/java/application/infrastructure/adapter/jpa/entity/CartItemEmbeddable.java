package application.infrastructure.adapter.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Persistence model for a {@code CartItem} line stored in the cart's element
 * collection. The money value object is flattened into primitive columns
 * (value objects are never mapped as embeddables).
 */
@Embeddable
@Getter
@Setter
public class CartItemEmbeddable {

    /** Surrogate key assigned by the database; nullable because legacy rows may lack one. */
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPriceAmount;

    @Column(name = "unit_price_currency", nullable = false, length = 3)
    private String unitPriceCurrency;
}
