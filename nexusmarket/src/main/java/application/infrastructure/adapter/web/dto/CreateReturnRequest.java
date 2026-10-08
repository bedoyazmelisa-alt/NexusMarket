package application.infrastructure.adapter.web.dto;

/** Request to create a return. */
public record CreateReturnRequest(Long orderId, String reason) {
}
