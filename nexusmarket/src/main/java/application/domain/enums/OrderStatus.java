package application.domain.enums;

/**
 * Order lifecycle per the documented flow:
 * Cart → Pending Payment → Paid → Dispatched → Delivered / Finalized (RG-10).
 * FINALIZED is terminal; a finalized order cannot be modified (RG-12).
 */
public enum OrderStatus {
    PENDING_PAYMENT,
    PAID,
    DISPATCHED,
    DELIVERED,
    FINALIZED
}