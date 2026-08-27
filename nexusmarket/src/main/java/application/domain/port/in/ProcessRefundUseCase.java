package application.domain.port.in;

import application.domain.model.Refund;
import application.domain.valueobject.Money;

/**
 * Input port: processes a refund associated with an approved return.
 */
public interface ProcessRefundUseCase {

    Refund processRefund(Long returnId, Money amount);
}