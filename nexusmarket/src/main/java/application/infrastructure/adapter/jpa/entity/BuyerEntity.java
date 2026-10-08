package application.infrastructure.adapter.jpa.entity;

import application.domain.enums.BuyerStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Persistence model for {@code Buyer}. Kept separate from the domain entity so
 * the domain stays free of database annotations (architectural constraint 13).
 */
@Entity
@Table(name = "buyers")
@Getter
@Setter
public class BuyerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Primary address, flattened into primitive columns.
    @Column(nullable = false, length = 255)
    private String street;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(nullable = false, length = 100)
    private String country;

    // Secondary addresses, stored one row each; EAGER because the adapter maps
    // to the domain after the repository transaction closes (open-in-view=false).
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "buyers_additional_addresses", joinColumns = @JoinColumn(name = "buyer_id"))
    private List<AddressEmbeddable> additionalAddresses;

    @Enumerated(EnumType.STRING)
    @Column(name = "commercial_status", nullable = false, length = 20)
    private BuyerStatus commercialStatus;
}
