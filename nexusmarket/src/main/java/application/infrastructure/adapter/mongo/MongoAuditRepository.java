package application.infrastructure.adapter.mongo;

import application.domain.model.AuditLog;
import application.domain.port.out.AuditRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * MongoDB implementation of {@link AuditRepository}. Translates between the
 * domain model and {@link AuditLogDocument}; no business rules live here.
 */
@Repository
@Profile("!in-memory")
public class MongoAuditRepository implements AuditRepository {

    private static final String COLLECTION_NAME = "audit_logs";

    private final AuditLogMongoRepository mongoRepository;
    private final MongoIdGenerator mongoIdGenerator;

    public MongoAuditRepository(AuditLogMongoRepository mongoRepository, MongoIdGenerator mongoIdGenerator) {
        this.mongoRepository = mongoRepository;
        this.mongoIdGenerator = mongoIdGenerator;
    }

    @Override
    public AuditLog save(AuditLog auditLog) {
        if (auditLog.getId() == null) {
            auditLog.assignId(mongoIdGenerator.nextId(COLLECTION_NAME));
        }
        mongoRepository.save(toDocument(auditLog));
        return auditLog;
    }

    private AuditLogDocument toDocument(AuditLog auditLog) {
        AuditLogDocument document = new AuditLogDocument();
        document.setId(auditLog.getId());
        document.setUserId(auditLog.getUserId());
        document.setAction(auditLog.getAction());
        document.setEntity(auditLog.getEntity());
        document.setEntityId(auditLog.getEntityId());
        document.setTimestamp(AuditLogDocument.toPersistedTimestamp(auditLog.getTimestamp()));
        document.setDetails(auditLog.getDetails());
        return document;
    }
}
