package application.infrastructure.adapter.jpa;

import application.infrastructure.adapter.jpa.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data repository for {@link SellerEntity}. Technical query layer only;
 * domain logic never sees this interface.
 */
public interface SellerJpaRepository extends JpaRepository<SellerEntity, Long> {

    Optional<SellerEntity> findByUserId(Long userId);
}
