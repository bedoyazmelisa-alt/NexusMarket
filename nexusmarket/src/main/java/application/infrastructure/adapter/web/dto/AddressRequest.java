package application.infrastructure.adapter.web.dto;

import application.domain.valueobject.Address;

/** Request carrying a delivery address. */
public record AddressRequest(String street, String city, String state,
                             String postalCode, String country) {

    /** Builds the domain {@link Address} represented by this request. */
    public Address toDomain() {
        return Address.of(street, city, state, postalCode, country);
    }
}
