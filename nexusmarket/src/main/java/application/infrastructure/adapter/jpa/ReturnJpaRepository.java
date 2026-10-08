package application.infrastructure.adapter.jpa;

import application.infrastructure.adapter.jpa.entity.ReturnEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for {@link ReturnEntity}. Technical query layer only;
 * domain logic never sees this interface.
 */
public interface ReturnJpaRepository extends JpaRepository<ReturnEntity, Long> {

    List<ReturnEntity> findByOrderId(Long orderId);
}
