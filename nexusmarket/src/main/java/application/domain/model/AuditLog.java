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

    /**
     * Rebuilds an audit log from persisted data. Unlike {@link #create}, it preserves
     * the stored state exactly (timestamp, id) and skips creation-time defaults;
     * used only by persistence adapters to rehydrate the domain.
     */
    public static AuditLog reconstitute(Long id, Long userId, String action, String entity,
                                        Long entityId, LocalDateTime timestamp, String details) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException("Action must not be blank");
        }
        if (entity == null || entity.isBlank()) {
            throw new IllegalArgumentException("Entity must not be blank");
        }
        AuditLog log = new AuditLog();
        log.id = id;
        log.userId = userId;
        log.action = action;
        log.entity = entity;
        log.entityId = entityId;
        log.timestamp = timestamp;
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