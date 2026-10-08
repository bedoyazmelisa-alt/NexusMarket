package application.infrastructure.adapter.jpa;

import application.domain.enums.CartStatus;
import application.infrastructure.adapter.jpa.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data repository for {@link CartEntity}. Technical query layer only;
 * domain logic never sees this interface.
 */
public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findFirstByBuyerIdAndStatus(Long buyerId, CartStatus status);
}
