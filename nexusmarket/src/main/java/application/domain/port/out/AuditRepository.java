package application.domain.port.out;

import application.domain.model.AuditLog;

/**
 * Output port for persisting operational traceability records.
 */
public interface AuditRepository {

    AuditLog save(AuditLog auditLog);
}