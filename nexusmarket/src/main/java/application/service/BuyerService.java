package application.service;

import application.domain.model.Buyer;
import application.domain.model.User;
import application.domain.port.in.RegisterBuyerUseCase;
import application.domain.port.out.BuyerRepository;
import application.domain.port.out.UserRepository;
import application.domain.valueobject.Address;

/**
 * Application service for buyer management. Implements
 * {@link RegisterBuyerUseCase}: the buyer must reference an existing user,
 * must own a mandatory primary address and a user can only hold one buyer
 * profile.
 */
public class BuyerService implements RegisterBuyerUseCase {

    private final BuyerRepository buyerRepository;
    private final UserRepository userRepository;

    public BuyerService(BuyerRepository buyerRepository, UserRepository userRepository) {
        this.buyerRepository = buyerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Buyer registerBuyer(Long userId, Address primaryAddress) {
        Buyer buyer = Buyer.create(userId, primaryAddress);
        userRepository.findById(buyer.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));
        if (buyerRepository.findByUserId(buyer.getUserId()).isPresent()) {
            throw new IllegalArgumentException(
                    "User " + buyer.getUserId() + " is already registered as a buyer");
        }
        return buyerRepository.save(buyer);
    }
}
