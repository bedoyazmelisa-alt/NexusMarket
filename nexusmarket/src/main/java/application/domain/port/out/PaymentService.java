package application.domain.port.out;

import application.domain.valueobject.Money;

/**
 * Output port for processing payments. Payment confirmation is required
 * before fulfillment (RG-11).
 */
public interface PaymentService {

    /**
     * @return {@code true} when the payment was confirmed.
     */
    boolean confirmPayment(Long orderId, Money amount);
}