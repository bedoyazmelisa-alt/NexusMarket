package application.infrastructure.adapter.jpa.entity;

import application.domain.enums.ShipmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Persistence model for {@code Shipment}. Kept separate from the domain entity so
 * the domain stays free of database annotations (architectural constraint 13).
 */
@Entity
@Table(name = "shipments")
@Getter
@Setter
public class ShipmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "tracking_number", nullable = false, length = 64)
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ShipmentStatus status;

    @Column(name = "shipment_date")
    private LocalDateTime shipmentDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;
}
