package application.infrastructure.adapter.jpa;

import application.infrastructure.adapter.jpa.entity.BuyerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data repository for {@link BuyerEntity}. Technical query layer only;
 * domain logic never sees this interface.
 */
public interface BuyerJpaRepository extends JpaRepository<BuyerEntity, Long> {

    Optional<BuyerEntity> findByUserId(Long userId);
}
