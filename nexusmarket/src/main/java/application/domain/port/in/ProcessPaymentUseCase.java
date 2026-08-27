package application.domain.port.in;

import application.domain.model.Order;

/**
 * Input port: confirms payment for an order. Payment confirmation is
 * required before fulfillment (RG-11).
 */
public interface ProcessPaymentUseCase {

    Order processPayment(Long orderId);
}