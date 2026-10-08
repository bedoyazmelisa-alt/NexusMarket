package application.infrastructure.adapter.web.dto;

/** Request to create a formal order from a cart. */
public record CreateOrderRequest(Long cartId) {
}
