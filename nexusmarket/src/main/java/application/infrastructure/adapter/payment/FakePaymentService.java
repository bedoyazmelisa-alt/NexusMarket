package application.infrastructure.adapter.payment;

import application.domain.port.out.PaymentService;
import application.domain.valueobject.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Payment adapter that confirms every payment. It is a stand-in for the real
 * payment gateway: the application still only marks an order as paid when
 * this returns {@code true} (RG-11), so the real adapter can be dropped in
 * without touching the domain.
 */
@Component
public class FakePaymentService implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(FakePaymentService.class);

    @Override
    public boolean confirmPayment(Long orderId, Money amount) {
        log.info("Confirming payment of {} for order {}", amount, orderId);
        return true;
    }
}
