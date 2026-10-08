package application.infrastructure.adapter.web.dto;

/** Request to create a cart for a buyer. */
public record CreateCartRequest(Long buyerId) {
}
