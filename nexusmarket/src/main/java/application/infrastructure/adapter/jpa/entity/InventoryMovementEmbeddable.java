package application.infrastructure.adapter.jpa.entity;

import application.domain.enums.InventoryMovementType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Persistence model for {@code InventoryMovement}. Value part of the inventory
 * aggregate, stored through an element collection; the owning inventory id
 * lives in the collection's join column, so it is not repeated here.
 */
@Embeddable
@Getter
@Setter
public class InventoryMovementEmbeddable {

    @Column(name = "movement_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 20)
    private InventoryMovementType movementType;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "movement_date", nullable = false)
    private LocalDateTime date;

    @Column(length = 500)
    private String reason;
}
