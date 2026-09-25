package application.service;

import application.domain.model.Cart;
import application.domain.port.in.CreateCartUseCase;
import application.domain.port.out.BuyerRepository;
import application.domain.port.out.CartRepository;

/**
 * Application service for the shopping cart. Implements
 * {@link CreateCartUseCase}: the cart belongs to an existing buyer and a buyer
 * only holds one open cart at a time, so an open cart is reused instead of
 * creating a duplicate.
 */
public class CartService implements CreateCartUseCase {

    private final CartRepository cartRepository;
    private final BuyerRepository buyerRepository;

    public CartService(CartRepository cartRepository, BuyerRepository buyerRepository) {
        this.cartRepository = cartRepository;
        this.buyerRepository = buyerRepository;
    }

    @Override
    public Cart createCart(Long buyerId) {
        if (buyerId == null) {
            throw new IllegalArgumentException("BuyerId must not be null");
        }
        buyerRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("Buyer does not exist"));
        return cartRepository.findOpenCartByBuyerId(buyerId)
                .orElseGet(() -> cartRepository.save(Cart.create(buyerId)));
    }
}
