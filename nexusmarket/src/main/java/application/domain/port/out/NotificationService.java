package application.domain.port.out;

import application.domain.model.User;

/**
 * Output port for sending notifications to users (e.g. order, shipment
 * and return events).
 */
public interface NotificationService {

    void notify(User user, String subject, String message);
}