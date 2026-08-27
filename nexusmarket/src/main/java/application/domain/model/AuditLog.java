package application.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Information required to maintain operational traceability. The exact audit
 * requirements must be refined according to technical and operational needs.
 */
@Getter
public class AuditLog {

    private Long id;
    private Long userId;
    private String action;
    private String entity;
    private Long entityId;
    private LocalDateTime timestamp;
    private String details;

    private AuditLog() {
    }

    public static AuditLog create(Long userId, String action, String entity, Long entityId, String details) {
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException("Action must not be blank");
        }
        if (entity == null || entity.isBlank()) {
            throw new IllegalArgumentException("Entity must not be blank");
        }
        AuditLog log = new AuditLog();
        log.userId = userId;
        log.action = action.trim();
        log.entity = entity.trim();
        log.entityId = entityId;
        log.timestamp = LocalDateTime.now();
        log.details = details;
        return log;
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}