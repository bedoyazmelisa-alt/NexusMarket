package application.domain.model;

import application.domain.enums.ProductStatus;
import application.domain.enums.ProductType;
import application.domain.exception.InvalidProductStateException;
import application.domain.valueobject.Money;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A good offered through the NexusMarket catalog. Products can be physical or
 * digital and must always have a valid status.
 */
@Getter
public class Product {

    private Long id;
    private Long sellerId;
    private String name;
    private String description;
    private ProductType productType;
    private ProductStatus status;
    private Money basePrice;
    private final List<ProductVariant> variants = new ArrayList<>();

    private Product() {
    }

    public static Product create(Long sellerId, String name, String description,
                                 ProductType productType, Money basePrice) {
        if (sellerId == null) {
            throw new IllegalArgumentException("SellerId must not be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (productType == null) {
            throw new IllegalArgumentException("Product type must not be null");
        }
        if (basePrice == null) {
            throw new IllegalArgumentException("Base price must not be null");
        }
        Product product = new Product();
        product.sellerId = sellerId;
        product.name = name.trim();
        product.description = description;
        product.productType = productType;
        product.basePrice = basePrice;
        product.status = ProductStatus.SUSPENDED;
        return product;
    }

    /**
     * Rebuilds a product from persisted data. Unlike {@link #create}, it preserves
     * the stored state exactly (status, variants, id) and skips creation-time
     * defaults; used only by persistence adapters to rehydrate the domain.
     */
    public static Product reconstitute(Long id, Long sellerId, String name, String description,
                                       ProductType productType, ProductStatus status, Money basePrice,
                                       List<ProductVariant> variants) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (sellerId == null) {
            throw new IllegalArgumentException("SellerId must not be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (productType == null) {
            throw new IllegalArgumentException("Product type must not be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
        if (basePrice == null) {
            throw new IllegalArgumentException("Base price must not be null");
        }
        Product product = new Product();
        product.id = id;
        product.sellerId = sellerId;
        product.name = name.trim();
        product.description = description;
        product.productType = productType;
        product.status = status;
        product.basePrice = basePrice;
        if (variants != null) {
            product.variants.addAll(variants);
        }
        return product;
    }

    public void publish() {
        if (status == ProductStatus.DISCONTINUED) {
            throw new InvalidProductStateException("A discontinued product cannot be published");
        }
        this.status = ProductStatus.PUBLISHED;
    }

    public void suspend() {
        if (status == ProductStatus.DISCONTINUED) {
            throw new InvalidProductStateException("A discontinued product cannot be suspended");
        }
        this.status = ProductStatus.SUSPENDED;
    }

    public void discontinue() {
        this.status = ProductStatus.DISCONTINUED;
    }

    public void changePrice(Money newPrice) {
        if (newPrice == null) {
            throw new IllegalArgumentException("Price must not be null");
        }
        if (status == ProductStatus.DISCONTINUED) {
            throw new InvalidProductStateException("A discontinued product cannot change price");
        }
        this.basePrice = newPrice;
    }

    public void addVariant(ProductVariant variant) {
        if (variant == null) {
            throw new IllegalArgumentException("Variant must not be null");
        }
        this.variants.add(variant);
    }

    public void removeVariant(ProductVariant variant) {
        if (variant == null) {
            throw new IllegalArgumentException("Variant must not be null");
        }
        if (!this.variants.remove(variant)) {
            throw new IllegalArgumentException("Variant does not exist");
        }
    }

    public List<ProductVariant> getVariants() {
        return Collections.unmodifiableList(variants);
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}