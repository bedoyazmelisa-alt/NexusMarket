package application.infrastructure.adapter.jpa.entity;

import application.domain.enums.SellerStatus;
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

/**
 * Persistence model for {@code Seller}. Kept separate from the domain entity so
 * the domain stays free of database annotations (architectural constraint 13).
 */
@Entity
@Table(name = "sellers")
@Getter
@Setter
public class SellerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "business_information", nullable = false, length = 1000)
    private String businessInformation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SellerStatus status;
}
