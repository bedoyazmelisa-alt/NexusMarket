package application.domain.model;

import application.domain.valueobject.Money;
import lombok.Getter;

/**
 * A product selected in a shopping cart. Quantity must be greater than zero.
 */
@Getter
public class CartItem {

    private Long id;
    private Long cartId;
    private Long productId;
    private int quantity;
    private Money unitPrice;

    private CartItem() {
    }

    public static CartItem create(Long cartId, Long productId, int quantity, Money unitPrice) {
        if (productId == null) {
            throw new IllegalArgumentException("ProductId must not be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("Unit price must not be null");
        }
        CartItem item = new CartItem();
        item.cartId = cartId;
        item.productId = productId;
        item.quantity = quantity;
        item.unitPrice = unitPrice;
        return item;
    }

    /**
     * Rebuilds a cart item from persisted data. Unlike {@link #create}, it preserves
     * the stored state exactly (id, cartId) and skips creation-time defaults;
     * used only by persistence adapters to rehydrate the domain.
     */
    public static CartItem reconstitute(Long id, Long cartId, Long productId,
                                        int quantity, Money unitPrice) {
        if (productId == null) {
            throw new IllegalArgumentException("ProductId must not be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("Unit price must not be null");
        }
        CartItem item = new CartItem();
        item.id = id;
        item.cartId = cartId;
        item.productId = productId;
        item.quantity = quantity;
        item.unitPrice = unitPrice;
        return item;
    }

    public void changeQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.quantity = newQuantity;
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}