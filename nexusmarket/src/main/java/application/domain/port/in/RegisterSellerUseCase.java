package application.domain.port.in;

import application.domain.model.Seller;
import application.domain.model.User;

/**
 * Input port: incorporates a seller. Sellers cannot self-register (RG-04);
 * registration must be performed by an Administrator (RG-05).
 */
public interface RegisterSellerUseCase {

    Seller registerSeller(User actor, Long userId, String businessInformation);
}