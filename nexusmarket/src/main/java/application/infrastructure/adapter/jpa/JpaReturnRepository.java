package application.infrastructure.adapter.jpa;

import application.domain.model.Return;
import application.domain.model.ReturnItem;
import application.domain.port.out.ReturnRepository;
import application.infrastructure.adapter.jpa.entity.ReturnEntity;
import application.infrastructure.adapter.jpa.entity.ReturnItemEmbeddable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MySQL/JPA implementation of {@link ReturnRepository}. Translates between the
 * persistence model ({@link ReturnEntity}) and the domain model; no business
 * rules live here.
 */
@Repository
@Profile("!in-memory")
public class JpaReturnRepository implements ReturnRepository {

    private final ReturnJpaRepository jpaRepository;

    public JpaReturnRepository(ReturnJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Return save(Return returnRequest) {
        ReturnEntity entity = toEntity(returnRequest);
        ReturnEntity saved = jpaRepository.save(entity);
        if (returnRequest.getId() == null) {
            returnRequest.assignId(saved.getId());
        }
        return returnRequest;
    }

    @Override
    public Optional<Return> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Return> findByOrderId(Long orderId) {
        if (orderId == null) {
            return List.of();
        }
        return jpaRepository.findByOrderId(orderId).stream().map(this::toDomain).toList();
    }

    private ReturnEntity toEntity(Return returnRequest) {
        ReturnEntity entity = new ReturnEntity();
        entity.setId(returnRequest.getId());
        entity.setOrderId(returnRequest.getOrderId());
        entity.setStatus(returnRequest.getStatus());
        entity.setReason(returnRequest.getReason());
        entity.setCreatedAt(returnRequest.getCreatedAt());
        entity.setProcessedAt(returnRequest.getProcessedAt());
        entity.setItems(returnRequest.getItems().stream().map(this::toEmbeddable).toList());
        return entity;
    }

    private ReturnItemEmbeddable toEmbeddable(ReturnItem item) {
        ReturnItemEmbeddable embeddable = new ReturnItemEmbeddable();
        embeddable.setId(item.getId());
        embeddable.setProductId(item.getProductId());
        embeddable.setQuantity(item.getQuantity());
        return embeddable;
    }

    private Return toDomain(ReturnEntity entity) {
        List<ReturnItem> items = entity.getItems() == null
                ? List.of()
                : entity.getItems().stream()
                        .map(embeddable -> toReturnItem(embeddable, entity.getId()))
                        .toList();
        return Return.reconstitute(
                entity.getId(),
                entity.getOrderId(),
                entity.getStatus(),
                entity.getReason(),
                entity.getCreatedAt(),
                entity.getProcessedAt(),
                items);
    }

    private ReturnItem toReturnItem(ReturnItemEmbeddable embeddable, Long returnId) {
        return ReturnItem.reconstitute(
                embeddable.getId(),
                returnId,
                embeddable.getProductId(),
                embeddable.getQuantity());
    }
}
