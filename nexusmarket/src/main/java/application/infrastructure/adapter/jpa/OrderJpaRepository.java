package application.infrastructure.adapter.jpa;

import application.infrastructure.adapter.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data repository for {@link OrderEntity}. Technical query layer only;
 * domain logic never sees this interface.
 */
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByBuyerId(Long buyerId);

    /**
     * Fetches orders with {@code start <= createdAt < end} (end exclusive),
     * matching the output port contract.
     */
    List<OrderEntity> findByCreatedAtGreaterThanEqualAndCreatedAtLessThan(LocalDateTime start, LocalDateTime end);
}
