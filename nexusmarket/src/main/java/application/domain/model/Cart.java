package application.domain.model;

import application.domain.enums.CartStatus;
import application.domain.exception.InvalidOrderStateException;
import application.domain.valueobject.Money;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A buyer's provisional selection of products before an order is formally
 * created. Checkout transforms the selection into a formal order.
 */
@Getter
public class Cart {

    private Long id;
    private Long buyerId;
    private CartStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final List<CartItem> items = new ArrayList<>();

    private Cart() {
    }

    public static Cart create(Long buyerId) {
        if (buyerId == null) {
            throw new IllegalArgumentException("BuyerId must not be null");
        }
        Cart cart = new Cart();
        cart.buyerId = buyerId;
        cart.status = CartStatus.OPEN;
        cart.createdAt = LocalDateTime.now();
        cart.updatedAt = cart.createdAt;
        return cart;
    }

    /**
     * Rebuilds a cart from persisted data. Unlike {@link #create}, it preserves
     * the stored state exactly (status, timestamps) and skips creation-time defaults;
     * used only by persistence adapters to rehydrate the domain.
     */
    public static Cart reconstitute(Long id, Long buyerId, CartStatus status,
                                    LocalDateTime createdAt, LocalDateTime updatedAt,
                                    List<CartItem> items) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (buyerId == null) {
            throw new IllegalArgumentException("BuyerId must not be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
        Cart cart = new Cart();
        cart.id = id;
        cart.buyerId = buyerId;
        cart.status = status;
        cart.createdAt = createdAt;
        cart.updatedAt = updatedAt;
        if (items != null) {
            cart.items.addAll(items);
        }
        return cart;
    }

    public void addItem(Long productId, int quantity, Money unitPrice) {
        requireOpen();
        if (productId == null) {
            throw new IllegalArgumentException("ProductId must not be null");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("Unit price must not be null");
        }
        CartItem existing = findItem(productId);
        if (existing != null) {
            existing.changeQuantity(existing.getQuantity() + quantity);
        } else {
            items.add(CartItem.create(id, productId, quantity, unitPrice));
        }
        touch();
    }

    public void removeItem(Long productId) {
        requireOpen();
        if (!items.removeIf(item -> item.getProductId().equals(productId))) {
            throw new IllegalArgumentException("Product is not in the cart");
        }
        touch();
    }

    public void changeQuantity(Long productId, int newQuantity) {
        requireOpen();
        CartItem item = findItem(productId);
        if (item == null) {
            throw new IllegalArgumentException("Product is not in the cart");
        }
        item.changeQuantity(newQuantity);
        touch();
    }

    public void clear() {
        requireOpen();
        items.clear();
        touch();
    }

    public void checkout() {
        requireOpen();
        if (items.isEmpty()) {
            throw new InvalidOrderStateException("Cannot checkout an empty cart");
        }
        this.status = CartStatus.CHECKED_OUT;
        touch();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    private CartItem findItem(Long productId) {
        return items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private void requireOpen() {
        if (status != CartStatus.OPEN) {
            throw new InvalidOrderStateException("Cart is not open");
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