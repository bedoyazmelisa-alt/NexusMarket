package application.service;

import application.domain.enums.OrderStatus;
import application.domain.exception.InvalidOrderStateException;
import application.domain.model.Order;
import application.domain.port.in.CreateOrderUseCase;
import application.domain.port.in.FinalizeOrderUseCase;
import application.domain.port.in.ProcessPaymentUseCase;
import application.domain.port.out.BuyerRepository;
import application.domain.port.out.CartRepository;
import application.domain.port.out.InventoryRepository;
import application.domain.port.out.NotificationService;
import application.domain.port.out.OrderRepository;
import application.domain.port.out.PaymentService;
import application.domain.port.out.UserRepository;
import application.domain.service.OrderCreationService;
import application.domain.service.OrderFinalizationService;
import application.domain.service.OrderValidationService;
import application.service.support.BuyerNotifier;

/**
 * Application service for the order lifecycle. Implements
 * {@link CreateOrderUseCase}, {@link ProcessPaymentUseCase} and
 * {@link FinalizeOrderUseCase}, delegating the business rules to the order
 * domain services: order creation from a cart with inventory reservation,
 * payment confirmation before fulfillment (RG-11) and finalization, after
 * which the order cannot be modified (RG-12).
 */
public class OrderService implements CreateOrderUseCase, ProcessPaymentUseCase, FinalizeOrderUseCase {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final OrderCreationService orderCreationService;
    private final OrderFinalizationService orderFinalizationService;
    private final OrderValidationService orderValidationService;
    private final BuyerNotifier buyerNotifier;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        InventoryRepository inventoryRepository,
                        PaymentService paymentService,
                        BuyerRepository buyerRepository,
                        UserRepository userRepository,
                        NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
        this.orderCreationService = new OrderCreationService(cartRepository, orderRepository, inventoryRepository);
        this.orderFinalizationService = new OrderFinalizationService(orderRepository);
        this.orderValidationService = new OrderValidationService();
        this.buyerNotifier = new BuyerNotifier(buyerRepository, userRepository, notificationService);
    }

    @Override
    public Order createOrder(Long cartId) {
        Order order = orderCreationService.createOrder(cartId);
        buyerNotifier.notifyBuyer(order.getBuyerId(), "Order created",
                "Your order " + order.getId() + " has been created and is awaiting payment.");
        return order;
    }

    @Override
    public Order processPayment(Long orderId) {
        Order order = requireOrder(orderId);
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new InvalidOrderStateException(
                    "Cannot process payment for an order in status " + order.getStatus());
        }
        // Never charge an order whose total is inconsistent with its items (RG-10).
        orderValidationService.validate(order);
        if (!paymentService.confirmPayment(order.getId(), order.getTotalAmount())) {
            throw new InvalidOrderStateException(
                    "Payment was not confirmed for order " + orderId + " (RG-11)");
        }
        order.markAsPaid();
        Order saved = orderRepository.save(order);
        buyerNotifier.notifyBuyer(saved.getBuyerId(), "Payment confirmed",
                "We received the payment for order " + saved.getId() + ".");
        return saved;
    }

    @Override
    public Order finalizeOrder(Long orderId) {
        Order order = requireOrder(orderId);
        // Last chance to detect an inconsistent order before it becomes immutable (RG-12).
        orderValidationService.validate(order);
        return orderFinalizationService.finalize(orderId);
    }

    private Order requireOrder(Long orderId) {
        if (orderId == null) {
            throw new InvalidOrderStateException("Order must exist");
        }
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidOrderStateException(
                        "Order with id " + orderId + " does not exist"));
    }
}
