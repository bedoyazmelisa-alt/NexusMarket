package application.infrastructure.adapter.web.dto;

/** Request to process a refund for an approved return. */
public record ProcessRefundRequest(Long returnId, MoneyRequest amount) {
}
