package application.infrastructure.adapter.jpa;

import application.domain.model.Order;
import application.domain.model.OrderItem;
import application.domain.port.out.OrderRepository;
import application.domain.valueobject.Money;
import application.infrastructure.adapter.jpa.entity.OrderEntity;
import application.infrastructure.adapter.jpa.entity.OrderItemEmbeddable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * MySQL/JPA implementation of {@link OrderRepository}. Translates between the
 * persistence model ({@link OrderEntity}) and the domain model; no business
 * rules live here.
 */
@Repository
@Profile("!in-memory")
public class JpaOrderRepository implements OrderRepository {

    private final OrderJpaRepository jpaRepository;

    public JpaOrderRepository(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity saved = jpaRepository.save(entity);
        if (order.getId() == null) {
            order.assignId(saved.getId());
        }
        return order;
    }

    @Override
    public Optional<Order> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Order> findByBuyerId(Long buyerId) {
        if (buyerId == null) {
            return List.of();
        }
        return jpaRepository.findByBuyerId(buyerId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return List.of();
        }
        return jpaRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThan(start, end)
                .stream().map(this::toDomain).toList();
    }

    private OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setBuyerId(order.getBuyerId());
        entity.setStatus(order.getStatus());
        if (order.getTotalAmount() != null) {
            entity.setTotalAmount(order.getTotalAmount().getAmount());
            entity.setTotalCurrency(order.getTotalAmount().getCurrency());
        }
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());
        entity.setItems(order.getItems().stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new)));
        return entity;
    }

    private OrderItemEmbeddable toEntity(OrderItem item) {
        OrderItemEmbeddable part = new OrderItemEmbeddable();
        part.setId(item.getId());
        part.setOrderId(item.getOrderId());
        part.setProductId(item.getProductId());
        part.setQuantity(item.getQuantity());
        part.setUnitPriceAmount(item.getUnitPrice().getAmount());
        part.setUnitPriceCurrency(item.getUnitPrice().getCurrency());
        return part;
    }

    private Order toDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems() == null
                ? List.of()
                : entity.getItems().stream().map(this::toDomain).toList();
        return Order.reconstitute(
                entity.getId(),
                entity.getBuyerId(),
                entity.getStatus(),
                toMoney(entity.getTotalAmount(), entity.getTotalCurrency()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                items);
    }

    private OrderItem toDomain(OrderItemEmbeddable part) {
        return OrderItem.reconstitute(
                part.getId(),
                part.getOrderId(),
                part.getProductId(),
                part.getQuantity(),
                Money.of(part.getUnitPriceAmount(), part.getUnitPriceCurrency()));
    }

    private Money toMoney(BigDecimal amount, String currency) {
        if (amount == null || currency == null) {
            return null;
        }
        return Money.of(amount, currency);
    }
}
