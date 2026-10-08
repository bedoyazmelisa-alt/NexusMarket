package application.infrastructure.adapter.web.dto;

import application.domain.enums.BuyerStatus;
import application.domain.model.Buyer;

import java.util.List;

/** Buyer view returned by the API (primitives only — no domain types). */
public record BuyerResponse(Long id, Long userId, AddressResponse primaryAddress,
                            List<AddressResponse> additionalAddresses,
                            BuyerStatus commercialStatus) {

    public static BuyerResponse from(Buyer buyer) {
        return new BuyerResponse(buyer.getId(), buyer.getUserId(),
                AddressResponse.from(buyer.getPrimaryAddress()),
                buyer.getAdditionalAddresses().stream().map(AddressResponse::from).toList(),
                buyer.getCommercialStatus());
    }
}
