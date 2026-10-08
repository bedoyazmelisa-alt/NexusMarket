package application.infrastructure.adapter.web;

import application.domain.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Maps domain and validation failures raised by controllers to HTTP responses.
 * Controllers stay free of try/catch noise and never leak stack traces.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return body(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(DomainException ex) {
        return body(HttpStatus.CONFLICT, ex);
    }

    private ResponseEntity<Map<String, String>> body(HttpStatus status, Exception ex) {
        return ResponseEntity.status(status).body(Map.of("message", ex.getMessage() == null ? status.getReasonPhrase() : ex.getMessage()));
    }
}
