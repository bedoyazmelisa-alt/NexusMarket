package application.service;

import application.domain.model.Seller;
import application.domain.model.User;
import application.domain.port.in.RegisterSellerUseCase;
import application.domain.port.out.SellerRepository;
import application.domain.port.out.UserRepository;
import application.domain.service.SellerRegistrationService;

/**
 * Application service for seller management. Implements
 * {@link RegisterSellerUseCase} by delegating the incorporation rules to the
 * {@link SellerRegistrationService} domain service (RG-04, RG-05) and making
 * sure the target user is not already a seller.
 */
public class SellerService implements RegisterSellerUseCase {

    private final SellerRepository sellerRepository;
    private final SellerRegistrationService sellerRegistrationService;

    public SellerService(SellerRepository sellerRepository, UserRepository userRepository) {
        this.sellerRepository = sellerRepository;
        this.sellerRegistrationService = new SellerRegistrationService(sellerRepository, userRepository);
    }

    @Override
    public Seller registerSeller(User actor, Long userId, String businessInformation) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null");
        }
        if (sellerRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException(
                    "User " + userId + " is already registered as a seller");
        }
        return sellerRegistrationService.register(actor, userId, businessInformation);
    }
}
