package application.infrastructure.adapter.jpa;

import application.domain.enums.CartStatus;
import application.domain.model.Cart;
import application.domain.model.CartItem;
import application.domain.port.out.CartRepository;
import application.domain.valueobject.Money;
import application.infrastructure.adapter.jpa.entity.CartEntity;
import application.infrastructure.adapter.jpa.entity.CartItemEmbeddable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MySQL/JPA implementation of {@link CartRepository}. Translates between the
 * persistence model ({@link CartEntity}) and the domain model; no business
 * rules live here.
 */
@Repository
@Profile("!in-memory")
public class JpaCartRepository implements CartRepository {

    private final CartJpaRepository jpaRepository;

    public JpaCartRepository(CartJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cart save(Cart cart) {
        CartEntity entity = toEntity(cart);
        CartEntity saved = jpaRepository.save(entity);
        if (cart.getId() == null) {
            cart.assignId(saved.getId());
        }
        return cart;
    }

    @Override
    public Optional<Cart> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Cart> findOpenCartByBuyerId(Long buyerId) {
        if (buyerId == null) {
            return Optional.empty();
        }
        return jpaRepository.findFirstByBuyerIdAndStatus(buyerId, CartStatus.OPEN)
                .map(this::toDomain);
    }

    private CartEntity toEntity(Cart cart) {
        CartEntity entity = new CartEntity();
        entity.setId(cart.getId());
        entity.setBuyerId(cart.getBuyerId());
        entity.setStatus(cart.getStatus());
        entity.setCreatedAt(cart.getCreatedAt());
        entity.setUpdatedAt(cart.getUpdatedAt());
        entity.setItems(cart.getItems().stream().map(this::toEmbeddable).toList());
        return entity;
    }

    private CartItemEmbeddable toEmbeddable(CartItem item) {
        CartItemEmbeddable embeddable = new CartItemEmbeddable();
        embeddable.setId(item.getId());
        embeddable.setProductId(item.getProductId());
        embeddable.setQuantity(item.getQuantity());
        embeddable.setUnitPriceAmount(item.getUnitPrice().getAmount());
        embeddable.setUnitPriceCurrency(item.getUnitPrice().getCurrency());
        return embeddable;
    }

    private Cart toDomain(CartEntity entity) {
        List<CartItem> items = entity.getItems() == null
                ? List.of()
                : entity.getItems().stream()
                        .map(embeddable -> toCartItem(embeddable, entity.getId()))
                        .toList();
        return Cart.reconstitute(
                entity.getId(),
                entity.getBuyerId(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                items);
    }

    private CartItem toCartItem(CartItemEmbeddable embeddable, Long cartId) {
        return CartItem.reconstitute(
                embeddable.getId(),
                cartId,
                embeddable.getProductId(),
                embeddable.getQuantity(),
                Money.of(embeddable.getUnitPriceAmount(), embeddable.getUnitPriceCurrency()));
    }
}
