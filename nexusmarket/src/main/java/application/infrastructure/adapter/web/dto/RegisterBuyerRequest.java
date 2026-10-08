package application.infrastructure.adapter.web.dto;

/** Request to register a buyer with its primary address. */
public record RegisterBuyerRequest(Long userId, AddressRequest primaryAddress) {
}
