package application.infrastructure.adapter.jpa;

import application.infrastructure.adapter.jpa.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data repository for {@link InvoiceEntity}. Technical query layer only;
 * domain logic never sees this interface.
 */
public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, Long> {

    Optional<InvoiceEntity> findByOrderId(Long orderId);
}
