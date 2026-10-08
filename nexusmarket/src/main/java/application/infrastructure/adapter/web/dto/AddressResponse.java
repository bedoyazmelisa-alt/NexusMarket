package application.infrastructure.adapter.web.dto;

import application.domain.valueobject.Address;

/** Address view returned by the API (primitives only — no domain types). */
public record AddressResponse(String street, String city, String state,
                              String postalCode, String country) {

    public static AddressResponse from(Address address) {
        return new AddressResponse(address.getStreet(), address.getCity(), address.getState(),
                address.getPostalCode(), address.getCountry());
    }
}
