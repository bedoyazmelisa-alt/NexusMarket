package application.infrastructure.adapter.jpa;

import application.domain.model.Refund;
import application.domain.port.out.RefundRepository;
import application.domain.valueobject.Money;
import application.infrastructure.adapter.jpa.entity.RefundEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MySQL/JPA implementation of {@link RefundRepository}. Translates between the
 * persistence model ({@link RefundEntity}) and the domain model; no business
 * rules live here.
 */
@Repository
@Profile("!in-memory")
public class JpaRefundRepository implements RefundRepository {

    private final RefundJpaRepository jpaRepository;

    public JpaRefundRepository(RefundJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Refund save(Refund refund) {
        RefundEntity entity = toEntity(refund);
        RefundEntity saved = jpaRepository.save(entity);
        if (refund.getId() == null) {
            refund.assignId(saved.getId());
        }
        return refund;
    }

    @Override
    public Optional<Refund> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Refund> findByReturnId(Long returnId) {
        if (returnId == null) {
            return Optional.empty();
        }
        return jpaRepository.findByReturnId(returnId).map(this::toDomain);
    }

    private RefundEntity toEntity(Refund refund) {
        RefundEntity entity = new RefundEntity();
        entity.setId(refund.getId());
        entity.setReturnId(refund.getReturnId());
        entity.setAmount(refund.getAmount().getAmount());
        entity.setCurrency(refund.getAmount().getCurrency());
        entity.setStatus(refund.getStatus());
        entity.setCreatedAt(refund.getCreatedAt());
        entity.setProcessedAt(refund.getProcessedAt());
        return entity;
    }

    private Refund toDomain(RefundEntity entity) {
        return Refund.reconstitute(
                entity.getId(),
                entity.getReturnId(),
                Money.of(entity.getAmount(), entity.getCurrency()),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getProcessedAt());
    }
}
