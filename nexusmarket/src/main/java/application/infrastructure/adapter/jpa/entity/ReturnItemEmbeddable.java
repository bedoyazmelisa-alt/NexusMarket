package application.infrastructure.adapter.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * Persistence model for a {@code ReturnItem} line stored in the return's
 * element collection.
 */
@Embeddable
@Getter
@Setter
public class ReturnItemEmbeddable {

    /** Surrogate key assigned by the database; nullable because legacy rows may lack one. */
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;
}
