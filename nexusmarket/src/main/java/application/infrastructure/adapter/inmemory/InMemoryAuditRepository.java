package application.infrastructure.adapter.inmemory;

import application.domain.model.AuditLog;
import application.domain.port.out.AuditRepository;
import org.springframework.stereotype.Repository;

/**
 * In-memory {@link AuditRepository}. Stand-in until the MongoDB adapter
 * exists. No application service records audit entries yet.
 */
@Repository
public class InMemoryAuditRepository extends InMemoryRepository<AuditLog> implements AuditRepository {

    public InMemoryAuditRepository() {
        super(AuditLog::getId, AuditLog::assignId);
    }

    @Override
    public AuditLog save(AuditLog auditLog) {
        return persist(auditLog);
    }
}
