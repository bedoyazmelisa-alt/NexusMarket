package application.domain.model;

import application.domain.enums.InvoiceStatus;
import application.domain.exception.InvalidOrderStateException;
import application.domain.valueobject.InvoiceNumber;
import application.domain.valueobject.Money;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Commercial billing information associated with a purchase. The exact
 * invoicing rules must be defined by detailed business requirements.
 */
@Getter
public class Invoice {

    private Long id;
    private Long orderId;
    private InvoiceNumber invoiceNumber;
    private LocalDate issueDate;
    private Money subtotal;
    private Money taxes;
    private Money total;
    private InvoiceStatus status;

    private Invoice() {
    }

    public static Invoice create(Long orderId, InvoiceNumber invoiceNumber,
                                 Money subtotal, Money taxes) {
        if (orderId == null) {
            throw new IllegalArgumentException("OrderId must not be null");
        }
        if (invoiceNumber == null) {
            throw new IllegalArgumentException("Invoice number must not be null");
        }
        if (subtotal == null || taxes == null) {
            throw new IllegalArgumentException("Subtotal and taxes must not be null");
        }
        Invoice invoice = new Invoice();
        invoice.orderId = orderId;
        invoice.invoiceNumber = invoiceNumber;
        invoice.issueDate = LocalDate.now();
        invoice.subtotal = subtotal;
        invoice.taxes = taxes;
        invoice.total = subtotal.add(taxes);
        invoice.status = InvoiceStatus.ISSUED;
        return invoice;
    }

    public void markAsPaid() {
        if (status == InvoiceStatus.CANCELLED) {
            throw new InvalidOrderStateException("A cancelled invoice cannot be marked as paid");
        }
        this.status = InvoiceStatus.PAID;
    }

    public void cancel() {
        if (status == InvoiceStatus.PAID) {
            throw new InvalidOrderStateException("A paid invoice cannot be cancelled");
        }
        this.status = InvoiceStatus.CANCELLED;
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}