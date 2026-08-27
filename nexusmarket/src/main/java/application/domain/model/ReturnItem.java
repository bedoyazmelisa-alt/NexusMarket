package application.domain.model;

import lombok.Getter;

/**
 * A product line included in a {@code Return} request.
 */
@Getter
public class ReturnItem {

    private Long id;
    private Long returnId;
    private Long productId;
    private int quantity;

    private ReturnItem() {
    }

    public static ReturnItem create(Long returnId, Long productId, int quantity) {
        if (productId == null) {
            throw new IllegalArgumentException("ProductId must not be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        ReturnItem item = new ReturnItem();
        item.returnId = returnId;
        item.productId = productId;
        item.quantity = quantity;
        return item;
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}