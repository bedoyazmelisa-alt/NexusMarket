package application.infrastructure.adapter.jpa.entity;

import application.domain.enums.ProductStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Persistence model for a product variant. Kept as an entity (not an embeddable)
 * because its attribute map must be an {@code @ElementCollection}, which is only
 * legal on entities; the owning product id is written through the association.
 */
@Entity
@Table(name = "products_variants")
@Getter
@Setter
public class ProductVariantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Written by the owning ProductEntity association; read back for reconstitution.
    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId;

    @Column(nullable = false, length = 200)
    private String name;

    @ElementCollection
    @CollectionTable(name = "product_variant_attributes",
            joinColumns = @JoinColumn(name = "variant_id"))
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

    /** List order within the owning product (managed by the adapter). */
    @Column(name = "position")
    private Integer position;
}
