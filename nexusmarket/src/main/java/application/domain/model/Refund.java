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

    /**
     * Rebuilds a refund from persisted data. Unlike {@link #create}, it preserves
     * the stored state exactly (timestamps, status, id) and skips creation-time
     * defaults; used only by persistence adapters to rehydrate the domain.
     */
    public static Refund reconstitute(Long id, Long returnId, Money amount,
                                      RefundStatus status, LocalDateTime createdAt,
                                      LocalDateTime processedAt) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (returnId == null) {
            throw new IllegalArgumentException("ReturnId must not be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("CreatedAt must not be null");
        }
        Refund refund = new Refund();
        refund.id = id;
        refund.returnId = returnId;
        refund.amount = amount;
        refund.status = status;
        refund.createdAt = createdAt;
        refund.processedAt = processedAt;
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