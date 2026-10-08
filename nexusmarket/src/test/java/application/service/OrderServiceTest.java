package application.service;

import application.domain.enums.OrderStatus;
import application.domain.exception.FinalizedOrderModificationException;
import application.domain.exception.InvalidOrderStateException;
import application.domain.model.Order;
import application.domain.service.OrderFinalizationService;
import application.domain.valueobject.Money;
import application.infrastructure.adapter.inmemory.InMemoryBuyerRepository;
import application.infrastructure.adapter.inmemory.InMemoryCartRepository;
import application.infrastructure.adapter.inmemory.InMemoryInventoryRepository;
import application.infrastructure.adapter.inmemory.InMemoryOrderRepository;
import application.infrastructure.adapter.inmemory.InMemoryUserRepository;
import application.infrastructure.adapter.notification.ConsoleNotificationService;
import application.infrastructure.adapter.payment.FakePaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Domain tests for the order lifecycle (RG-10, RG-11, RG-12): only valid
 * state transitions are accepted and a finalized order cannot be modified.
 */
class OrderServiceTest {

    private OrderService orderService;
    private InMemoryOrderRepository orderRepository;
    private OrderFinalizationService orderFinalizationService;

    @BeforeEach
    void setUp() {
        orderRepository = new InMemoryOrderRepository();
        orderService = new OrderService(
                orderRepository,
                new InMemoryCartRepository(),
                new InMemoryInventoryRepository(),
                new FakePaymentService(),
                new InMemoryBuyerRepository(),
                new InMemoryUserRepository(),
                new ConsoleNotificationService());
        orderFinalizationService = new OrderFinalizationService(orderRepository);
    }

    @Test
    void shouldRequireValidOrderStateTransition() {
        // Service level: an order already in PAID cannot go through payment again.
        Order paidOrder = createValidOrder();
        paidOrder.markAsPaid(); // PENDING_PAYMENT -> PAID is valid
        orderRepository.save(paidOrder);

        InvalidOrderStateException paymentAgain = assertThrows(
                InvalidOrderStateException.class,
                () -> orderService.processPayment(paidOrder.getId()));
        assertTrue(paymentAgain.getMessage().contains("PAID"));
        assertEquals(OrderStatus.PAID, currentOrder(paidOrder.getId()).getStatus());

        // Domain level: skipping a lifecycle step is rejected (RG-10).
        Order freshOrder = createValidOrder();
        InvalidOrderStateException skippedStep = assertThrows(
                InvalidOrderStateException.class,
                freshOrder::markAsDispatched);
        assertTrue(skippedStep.getMessage().contains("PENDING_PAYMENT"));
        assertEquals(OrderStatus.PENDING_PAYMENT, freshOrder.getStatus());
    }

    @Test
    void shouldNotModifyFinalizedOrder() {
        Order order = createValidOrder();
        order.markAsPaid();
        order.markAsDispatched();
        order.markAsDelivered();
        order.finalize(); // DELIVERED -> FINALIZED is valid
        orderRepository.save(order);
        assertEquals(OrderStatus.FINALIZED, currentOrder(order.getId()).getStatus());

        // A finalized order cannot be modified (RG-12).
        FinalizedOrderModificationException exception = assertThrows(
                FinalizedOrderModificationException.class,
                () -> orderFinalizationService.addItem(order.getId(), 300L, 1,
                        Money.of("10.00", "USD")));
        assertTrue(exception.getMessage().contains("finalized"));

        Order stored = currentOrder(order.getId());
        assertEquals(OrderStatus.FINALIZED, stored.getStatus());
        assertEquals(1, stored.getItems().size());
    }

    @Test
    void shouldMarkOrderAsPaidWhenPaymentIsConfirmed() {
        Order order = createValidOrder();

        Order paid = orderService.processPayment(order.getId());

        assertEquals(OrderStatus.PAID, paid.getStatus());
        assertEquals(OrderStatus.PAID, currentOrder(order.getId()).getStatus());
    }

    private Order createValidOrder() {
        Order order = Order.create(100L);
        order.addItem(200L, 2, Money.of("25.00", "USD"));
        order.calculateTotal();
        return orderRepository.save(order);
    }

    private Order currentOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new AssertionError("Order must exist: " + orderId));
    }
}
