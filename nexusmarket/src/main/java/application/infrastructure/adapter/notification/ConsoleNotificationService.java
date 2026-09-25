package application.infrastructure.adapter.notification;

import application.domain.model.User;
import application.domain.port.out.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Notification adapter that writes to the application log instead of sending
 * email/push messages. Stand-in until a real notification provider exists.
 */
@Component
public class ConsoleNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(ConsoleNotificationService.class);

    @Override
    public void notify(User user, String subject, String message) {
        log.info("[notification] to={} <{}> subject=\"{}\" body=\"{}\"",
                user.getFullName(), user.getEmail().getValue(), subject, message);
    }
}
