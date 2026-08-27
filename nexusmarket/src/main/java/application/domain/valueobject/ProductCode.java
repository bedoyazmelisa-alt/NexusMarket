package application.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Stable business code that identifies a {@code Product} in the catalog.
 */
@Getter
@EqualsAndHashCode
@ToString
public final class ProductCode {

    private final String value;

    private ProductCode(String value) {
        this.value = value;
    }

    public static ProductCode of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Product code must not be blank");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("Product code must not exceed 50 characters");
        }
        return new ProductCode(value.trim());
    }
}