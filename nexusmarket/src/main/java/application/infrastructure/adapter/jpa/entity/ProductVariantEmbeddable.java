package application.infrastructure.adapter.jpa.entity;

import application.domain.enums.ProductStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Persistence model for {@code ProductVariant}. Value part of the product
 * aggregate, stored through an element collection; the owning product id lives
 * in the collection's join column, so it is not repeated here.
 */
@Embeddable
@Getter
@Setter
public class ProductVariantEmbeddable {

    @Column(name = "variant_id")
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @ElementCollection
    @CollectionTable(name = "product_variant_attributes",
            joinColumns = @JoinColumn(name = "variant_ref"))
    @MapKeyColumn(name = "attribute_key", nullable = false, length = 100)
    @Column(name = "attribute_value", nullable = false, length = 500)
    private Map<String, String> attributes = new LinkedHashMap<>();

    @Column(name = "price_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal priceAmount;

    @Column(name = "price_currency", nullable = false, length = 3)
    private String priceCurrency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status;
}
