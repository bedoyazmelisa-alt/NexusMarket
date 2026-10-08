package application.infrastructure.adapter.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for {@link AuditLogDocument} on the
 * {@code audit_logs} collection.
 */
public interface AuditLogMongoRepository extends MongoRepository<AuditLogDocument, Long> {
}
