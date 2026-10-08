package application.domain.model;

import application.domain.enums.ProductStatus;
import application.domain.valueobject.Money;
import lombok.Getter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A specific variation of a product (e.g. color, size, model). A variant must
 * belong to a product and stay consistent with its parent product.
 */
@Getter
public class ProductVariant {

    private Long id;
    private Long productId;
    private String name;
    private final Map<String, String> attributes = new LinkedHashMap<>();
    private Money price;
    private ProductStatus status;

    private ProductVariant() {
    }

    public static ProductVariant create(Long productId, String name, Map<String, String> attributes, Money price) {
        if (productId == null) {
            throw new IllegalArgumentException("ProductId must not be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price must not be null");
        }
        ProductVariant variant = new ProductVariant();
        variant.productId = productId;
        variant.name = name.trim();
        if (attributes != null) {
            variant.attributes.putAll(attributes);
        }
        variant.price = price;
        variant.status = ProductStatus.SUSPENDED;
        return variant;
    }

    /**
     * Rebuilds a variant from persisted data. Unlike {@link #create}, it preserves
     * the stored state exactly (status, id) and skips creation-time defaults;
     * used only by persistence adapters to rehydrate the domain.
     */
    public static ProductVariant reconstitute(Long id, Long productId, String name,
                                              Map<String, String> attributes, Money price,
                                              ProductStatus status) {
        if (productId == null) {
            throw new IllegalArgumentException("ProductId must not be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price must not be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
        ProductVariant variant = new ProductVariant();
        variant.id = id;
        variant.productId = productId;
        variant.name = name.trim();
        if (attributes != null) {
            variant.attributes.putAll(attributes);
        }
        variant.price = price;
        variant.status = status;
        return variant;
    }

    public void changePrice(Money newPrice) {
        if (newPrice == null) {
            throw new IllegalArgumentException("Price must not be null");
        }
        this.price = newPrice;
    }

    public void publish() {
        this.status = ProductStatus.PUBLISHED;
    }

    public void suspend() {
        this.status = ProductStatus.SUSPENDED;
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}