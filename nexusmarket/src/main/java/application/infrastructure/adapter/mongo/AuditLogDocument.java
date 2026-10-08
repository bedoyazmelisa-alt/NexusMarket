package application.infrastructure.adapter.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * MongoDB persistence model for {@code AuditLog}. Kept separate from the domain
 * entity so the domain stays free of database annotations; timestamps are stored
 * as {@link Date} and converted explicitly with the system zone.
 */
@Getter
@Setter
@Document(collection = "audit_logs")
public class AuditLogDocument {

    @Id
    private Long id;

    private Long userId;
    private String action;
    private String entity;
    private Long entityId;
    private Date timestamp;
    private String details;

    public static Date toPersistedTimestamp(LocalDateTime timestamp) {
        if (timestamp == null) {
            return null;
        }
        return Date.from(timestamp.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toDomainTimestamp(Date timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
