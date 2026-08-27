package application.domain.service;

import application.domain.enums.UserRole;
import application.domain.exception.SellerRegistrationNotAllowedException;
import application.domain.model.Seller;
import application.domain.model.User;
import application.domain.port.out.SellerRepository;
import application.domain.port.out.UserRepository;

/**
 * Domain service responsible for incorporating a seller. Enforces RG-04
 * (sellers cannot self-register) and RG-05 (registration must be performed
 * by an Administrator).
 */
public class SellerRegistrationService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;

    public SellerRegistrationService(SellerRepository sellerRepository, UserRepository userRepository) {
        this.sellerRepository = sellerRepository;
        this.userRepository = userRepository;
    }

    public Seller register(User actor, Long userId, String businessInformation) {
        if (actor == null) {
            throw new SellerRegistrationNotAllowedException("An authenticated user is required (RG-01)");
        }
        if (actor.getRole() != UserRole.ADMINISTRATOR) {
            throw new SellerRegistrationNotAllowedException(
                    "Only an Administrator can register a seller (RG-05)");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null");
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        Seller seller = Seller.create(userId, businessInformation);
        return sellerRepository.save(seller);
    }
}