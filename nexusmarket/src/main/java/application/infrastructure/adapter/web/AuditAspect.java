package application.infrastructure.adapter.web;

import application.domain.model.AuditLog;
import application.domain.port.out.AuditRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Records every mutating API call (POST/PUT/PATCH/DELETE) into the audit
 * trail through the {@link AuditRepository} output port. Audit failures are
 * logged but never break the request being served.
 *
 * <p>The acting user is read from an optional {@code X-User-Id} request
 * header; the domain has no credential model yet, so it may be absent.</p>
 */
@Aspect
@Component
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);
    private static final String USER_ID_HEADER = "X-User-Id";

    private final AuditRepository auditRepository;

    public AuditAspect(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping)"
            + " || @annotation(org.springframework.web.bind.annotation.PutMapping)"
            + " || @annotation(org.springframework.web.bind.annotation.PatchMapping)"
            + " || @annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        try {
            HttpServletRequest request = currentRequest();
            String action = request.getMethod() + "." + joinPoint.getSignature().getName();
            String details = request.getMethod() + " " + request.getRequestURI();
            auditRepository.save(AuditLog.create(
                    currentUserId(request),
                    action,
                    joinPoint.getTarget().getClass().getSimpleName(),
                    firstIdArgument(joinPoint),
                    details));
        } catch (RuntimeException ex) {
            log.warn("Audit logging failed: {}", ex.getMessage());
        }
        return result;
    }

    private HttpServletRequest currentRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No request in scope");
        }
        return attributes.getRequest();
    }

    private Long currentUserId(HttpServletRequest request) {
        String header = request.getHeader(USER_ID_HEADER);
        if (header == null || header.isBlank()) {
            return null;
        }
        try {
            return Long.valueOf(header.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Long firstIdArgument(ProceedingJoinPoint joinPoint) {
        for (Object argument : joinPoint.getArgs()) {
            if (argument instanceof Long id) {
                return id;
            }
        }
        return null;
    }
}
