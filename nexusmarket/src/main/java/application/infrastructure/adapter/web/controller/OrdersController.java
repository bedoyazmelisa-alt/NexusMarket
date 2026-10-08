package application.infrastructure.adapter.web.controller;

import application.domain.model.Order;
import application.domain.port.in.CreateOrderUseCase;
import application.domain.port.in.FinalizeOrderUseCase;
import application.domain.port.in.ProcessPaymentUseCase;
import application.infrastructure.adapter.web.dto.CreateOrderRequest;
import application.infrastructure.adapter.web.dto.OrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes {@link CreateOrderUseCase}, {@link ProcessPaymentUseCase} and
 * {@link FinalizeOrderUseCase} over HTTP. */
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final CreateOrderUseCase createOrder;
    private final ProcessPaymentUseCase processPayment;
    private final FinalizeOrderUseCase finalizeOrder;

    public OrdersController(CreateOrderUseCase createOrder,
                            ProcessPaymentUseCase processPayment,
                            FinalizeOrderUseCase finalizeOrder) {
        this.createOrder = createOrder;
        this.processPayment = processPayment;
        this.finalizeOrder = finalizeOrder;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody CreateOrderRequest request) {
        Order order = createOrder.createOrder(request.cartId());
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.from(order));
    }

    @PostMapping("/{orderId}/payment")
    public ResponseEntity<OrderResponse> pay(@PathVariable Long orderId) {
        Order order = processPayment.processPayment(orderId);
        return ResponseEntity.ok(OrderResponse.from(order));
    }

    @PatchMapping("/{orderId}/finalize")
    public ResponseEntity<OrderResponse> finalize(@PathVariable Long orderId) {
        Order order = finalizeOrder.finalizeOrder(orderId);
        return ResponseEntity.ok(OrderResponse.from(order));
    }
}
