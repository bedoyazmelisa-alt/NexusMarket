package application.infrastructure.adapter.web.dto;

import application.domain.enums.RefundStatus;
import application.domain.model.Refund;
import application.domain.valueobject.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Refund view returned by the API (primitives only — no domain types). */
public record RefundResponse(Long id, Long returnId, MoneyResponse amount, RefundStatus status,
                             LocalDateTime createdAt, LocalDateTime processedAt) {

    public static RefundResponse from(Refund refund) {
        return new RefundResponse(refund.getId(), refund.getReturnId(),
                MoneyResponse.from(refund.getAmount()), refund.getStatus(),
                refund.getCreatedAt(), refund.getProcessedAt());
    }

    /** Monetary amount exposed by the API. */
    public record MoneyResponse(BigDecimal amount, String currency) {
        public static MoneyResponse from(Money money) {
            return new MoneyResponse(money.getAmount(), money.getCurrency());
        }
    }
}
