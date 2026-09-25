package application.infrastructure.adapter.inmemory;

import application.domain.model.Return;
import application.domain.port.out.ReturnRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * In-memory {@link ReturnRepository}. Stand-in until the MySQL adapter exists.
 */
@Repository
public class InMemoryReturnRepository extends InMemoryRepository<Return> implements ReturnRepository {

    public InMemoryReturnRepository() {
        super(Return::getId, Return::assignId);
    }

    @Override
    public Return save(Return returnRequest) {
        return persist(returnRequest);
    }

    @Override
    public Optional<Return> findById(Long id) {
        return row(id);
    }

    @Override
    public List<Return> findByOrderId(Long orderId) {
        if (orderId == null) {
            return List.of();
        }
        return all().stream().filter(returnRequest -> orderId.equals(returnRequest.getOrderId())).toList();
    }
}
