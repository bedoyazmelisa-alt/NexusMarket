package application.infrastructure.adapter.jpa;

import application.infrastructure.adapter.jpa.entity.RefundEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data repository for {@link RefundEntity}. Technical query layer only;
 * domain logic never sees this interface.
 */
public interface RefundJpaRepository extends JpaRepository<RefundEntity, Long> {

    Optional<RefundEntity> findByReturnId(Long returnId);
}
