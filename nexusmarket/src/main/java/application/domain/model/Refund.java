package application.domain.model;

import application.domain.enums.RefundStatus;
import application.domain.valueobject.Money;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * The reimbursement associated with an approved return or another applicable
 * business operation. Detailed refund rules must be defined by requirements.
 */
@Getter
public class Refund {

    private Long id;
    private Long returnId;
    private Money amount;
    private RefundStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    private Refund() {
    }

    public static Refund create(Long returnId, Money amount) {
        if (returnId == null) {
            throw new IllegalArgumentException("ReturnId must not be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        Refund refund = new Refund();
        refund.returnId = returnId;
        refund.amount = amount;
        refund.status = RefundStatus.PENDING;
        refund.createdAt = LocalDateTime.now();
        return refund;
    }

    public void markProcessed() {
        if (status != RefundStatus.PENDING) {
            throw new IllegalStateException("Refund is not pending");
        }
        this.status = RefundStatus.PROCESSED;
        this.processedAt = LocalDateTime.now();
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}