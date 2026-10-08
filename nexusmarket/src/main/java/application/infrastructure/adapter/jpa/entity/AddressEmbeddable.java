package application.infrastructure.adapter.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * Persistence model for an {@code Address} stored in a collection table
 * (value objects themselves never carry persistence annotations).
 */
@Embeddable
@Getter
@Setter
public class AddressEmbeddable {

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
}
