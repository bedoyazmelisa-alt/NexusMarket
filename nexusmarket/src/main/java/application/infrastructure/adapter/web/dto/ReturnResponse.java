package application.infrastructure.adapter.web.dto;

import application.domain.enums.ReturnStatus;
import application.domain.model.Return;

import java.time.LocalDateTime;

/** Return view returned by the API (primitives only — no domain types). */
public record ReturnResponse(Long id, Long orderId, ReturnStatus status, String reason,
                             LocalDateTime createdAt, LocalDateTime processedAt) {
    public static ReturnResponse from(Return created) {
        return new ReturnResponse(created.getId(), created.getOrderId(), created.getStatus(),
                created.getReason(), created.getCreatedAt(), created.getProcessedAt());
    }
}
