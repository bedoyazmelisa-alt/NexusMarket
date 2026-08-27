package application.domain.port.in;

import application.domain.model.Buyer;
import application.domain.valueobject.Address;

/**
 * Input port: registers a buyer with a mandatory primary address.
 */
public interface RegisterBuyerUseCase {

    Buyer registerBuyer(Long userId, Address primaryAddress);
}