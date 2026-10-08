package application.infrastructure.adapter.web.dto;

import application.domain.enums.SellerStatus;
import application.domain.model.Seller;

/** Seller view returned by the API (primitives only — no domain types). */
public record SellerResponse(Long id, Long userId, String businessInformation, SellerStatus status) {

    public static SellerResponse from(Seller seller) {
        return new SellerResponse(seller.getId(), seller.getUserId(),
                seller.getBusinessInformation(), seller.getStatus());
    }
}
