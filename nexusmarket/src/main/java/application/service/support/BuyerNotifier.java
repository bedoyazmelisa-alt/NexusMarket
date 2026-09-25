package application.service.support;

import application.domain.model.Buyer;
import application.domain.model.User;
import application.domain.port.out.BuyerRepository;
import application.domain.port.out.NotificationService;
import application.domain.port.out.UserRepository;

/**
 * Resolves the user behind a buyer and sends it a notification through the
 * {@link NotificationService} output port.
 *
 * <p>Notifications are best-effort: a missing buyer or user must never roll
 * back the business operation that triggered the event.</p>
 */
public class BuyerNotifier {

    private final BuyerRepository buyerRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public BuyerNotifier(BuyerRepository buyerRepository,
                         UserRepository userRepository,
                         NotificationService notificationService) {
        this.buyerRepository = buyerRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public void notifyBuyer(Long buyerId, String subject, String message) {
        if (buyerId == null) {
            return;
        }
        Buyer buyer = buyerRepository.findById(buyerId).orElse(null);
        if (buyer == null) {
            return;
        }
        User user = userRepository.findById(buyer.getUserId()).orElse(null);
        if (user == null) {
            return;
        }
        notificationService.notify(user, subject, message);
    }
}
