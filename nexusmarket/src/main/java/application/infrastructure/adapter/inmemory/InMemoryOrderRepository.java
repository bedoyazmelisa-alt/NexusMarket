package application.infrastructure.adapter.inmemory;

import application.domain.model.Order;
import application.domain.port.out.OrderRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * In-memory {@link OrderRepository}. Stand-in until the MySQL adapter exists.
 */
@Repository
public class InMemoryOrderRepository extends InMemoryRepository<Order> implements OrderRepository {

    public InMemoryOrderRepository() {
        super(Order::getId, Order::assignId);
    }

    @Override
    public Order save(Order order) {
        return persist(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return row(id);
    }

    @Override
    public List<Order> findByBuyerId(Long buyerId) {
        if (buyerId == null) {
            return List.of();
        }
        return all().stream().filter(order -> buyerId.equals(order.getBuyerId())).toList();
    }

    @Override
    public List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return List.of();
        }
        return all().stream()
                .filter(order -> order.getCreatedAt() != null)
                .filter(order -> !order.getCreatedAt().isBefore(start))
                .filter(order -> order.getCreatedAt().isBefore(end))
                .toList();
    }
}
