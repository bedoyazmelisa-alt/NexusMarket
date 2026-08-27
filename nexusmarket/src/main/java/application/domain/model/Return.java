package application.domain.model;

import application.domain.enums.ReturnStatus;
import application.domain.exception.InvalidReturnException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A product return requested or processed after a purchase. Detailed
 * eligibility and approval rules must be defined before adding more
 * specific business behavior.
 */
@Getter
public class Return {

    private Long id;
    private Long orderId;
    private ReturnStatus status;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private final List<ReturnItem> items = new ArrayList<>();

    private Return() {
    }

    public static Return create(Long orderId, String reason) {
        if (orderId == null) {
            throw new IllegalArgumentException("OrderId must not be null");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason must not be blank");
        }
        Return returnRequest = new Return();
        returnRequest.orderId = orderId;
        returnRequest.reason = reason.trim();
        returnRequest.status = ReturnStatus.REQUESTED;
        returnRequest.createdAt = LocalDateTime.now();
        return returnRequest;
    }

    public void addItem(Long productId, int quantity) {
        if (status != ReturnStatus.REQUESTED) {
            throw new InvalidReturnException("Items can only be added to a requested return");
        }
        items.add(ReturnItem.create(id, productId, quantity));
    }

    public void approve() {
        requireStatus(ReturnStatus.REQUESTED, "approve");
        this.status = ReturnStatus.APPROVED;
    }

    public void reject() {
        requireStatus(ReturnStatus.REQUESTED, "reject");
        this.status = ReturnStatus.REJECTED;
    }

    public void markProcessed() {
        requireStatus(ReturnStatus.APPROVED, "mark as processed");
        this.status = ReturnStatus.PROCESSED;
        this.processedAt = LocalDateTime.now();
    }

    public List<ReturnItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    private void requireStatus(ReturnStatus expected, String operation) {
        if (status != expected) {
            throw new InvalidReturnException(
                    "Cannot " + operation + " a return in status " + status);
        }
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}