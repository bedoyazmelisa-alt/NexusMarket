package application.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Carrier tracking number assigned to a {@code Shipment}.
 */
@Getter
@EqualsAndHashCode
@ToString
public final class TrackingNumber {

    private final String value;

    private TrackingNumber(String value) {
        this.value = value;
    }

    public static TrackingNumber of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Tracking number must not be blank");
        }
        return new TrackingNumber(value.trim());
    }
}