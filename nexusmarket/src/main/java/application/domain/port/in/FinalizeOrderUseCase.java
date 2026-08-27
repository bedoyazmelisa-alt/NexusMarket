package application.domain.port.in;

import application.domain.model.Order;

/**
 * Input port: finalizes an order. A finalized order cannot be modified (RG-12).
 */
public interface FinalizeOrderUseCase {

    Order finalizeOrder(Long orderId);
}