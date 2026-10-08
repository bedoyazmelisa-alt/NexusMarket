package application.infrastructure.adapter.web.dto;

import application.domain.enums.CartStatus;
import application.domain.model.Cart;

/** Cart view returned by the API (primitives only — no domain types). */
public record CartResponse(Long id, Long buyerId, CartStatus status) {

    public static CartResponse from(Cart cart) {
        return new CartResponse(cart.getId(), cart.getBuyerId(), cart.getStatus());
    }
}
