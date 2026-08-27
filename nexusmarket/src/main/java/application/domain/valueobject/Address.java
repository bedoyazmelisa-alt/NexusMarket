package application.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a delivery location. Compared by its values, not by identity.
 */
@Getter
@EqualsAndHashCode
@ToString
public final class Address {

    private final String street;
    private final String city;
    private final String state;
    private final String postalCode;
    private final String country;

    private Address(String street, String city, String state, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    public static Address of(String street, String city, String state, String postalCode, String country) {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street must not be blank");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City must not be blank");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country must not be blank");
        }
        return new Address(street.trim(), city.trim(), state, postalCode, country.trim());
    }
}