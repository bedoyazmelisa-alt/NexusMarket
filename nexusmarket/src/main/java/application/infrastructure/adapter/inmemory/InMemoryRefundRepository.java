package application.infrastructure.adapter.inmemory;

import application.domain.model.Refund;
import application.domain.port.out.RefundRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * In-memory {@link RefundRepository}. Stand-in until the MySQL adapter exists.
 */
@Repository
public class InMemoryRefundRepository extends InMemoryRepository<Refund> implements RefundRepository {

    public InMemoryRefundRepository() {
        super(Refund::getId, Refund::assignId);
    }

    @Override
    public Refund save(Refund refund) {
        return persist(refund);
    }

    @Override
    public Optional<Refund> findById(Long id) {
        return row(id);
    }

    @Override
    public Optional<Refund> findByReturnId(Long returnId) {
        if (returnId == null) {
            return Optional.empty();
        }
        return all().stream().filter(refund -> returnId.equals(refund.getReturnId())).findFirst();
    }
}
