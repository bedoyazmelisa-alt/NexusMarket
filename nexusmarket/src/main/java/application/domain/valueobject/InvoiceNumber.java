package application.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Business invoice number assigned to an {@code Invoice}.
 */
@Getter
@EqualsAndHashCode
@ToString
public final class InvoiceNumber {

    private final String value;

    private InvoiceNumber(String value) {
        this.value = value;
    }

    public static InvoiceNumber of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Invoice number must not be blank");
        }
        return new InvoiceNumber(value.trim());
    }
}