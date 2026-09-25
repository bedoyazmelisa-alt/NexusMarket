package application.service;

import application.domain.enums.OrderStatus;
import application.domain.exception.InvalidOrderStateException;
import application.domain.model.Order;
import application.domain.model.Shipment;
import application.domain.port.in.CreateShipmentUseCase;
import application.domain.port.out.BuyerRepository;
import application.domain.port.out.NotificationService;
import application.domain.port.out.OrderRepository;
import application.domain.port.out.ShipmentRepository;
import application.domain.port.out.UserRepository;
import application.domain.port.out.WarehouseRepository;
import application.service.support.BuyerNotifier;
import application.domain.valueobject.TrackingNumber;

/**
 * Application service for logistics. Implements {@link CreateShipmentUseCase}:
 * a shipment requires an existing, paid order (RG-11) and an existing
 * warehouse, and starts in PREPARING status until it is dispatched.
 */
public class ShipmentService implements CreateShipmentUseCase {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;
    private final WarehouseRepository warehouseRepository;
    private final BuyerNotifier buyerNotifier;

    public ShipmentService(ShipmentRepository shipmentRepository,
                           OrderRepository orderRepository,
                           WarehouseRepository warehouseRepository,
                           BuyerRepository buyerRepository,
                           UserRepository userRepository,
                           NotificationService notificationService) {
        this.shipmentRepository = shipmentRepository;
        this.orderRepository = orderRepository;
        this.warehouseRepository = warehouseRepository;
        this.buyerNotifier = new BuyerNotifier(buyerRepository, userRepository, notificationService);
    }

    @Override
    public Shipment createShipment(Long orderId, Long warehouseId, TrackingNumber trackingNumber) {
        Order order = requireOrder(orderId);
        if (order.getStatus() != OrderStatus.PAID && order.getStatus() != OrderStatus.DISPATCHED) {
            throw new InvalidOrderStateException(
                    "A shipment requires a paid order (RG-11), current status: " + order.getStatus());
        }
        warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse does not exist"));

        Shipment shipment = Shipment.create(orderId, warehouseId, trackingNumber);
        Shipment saved = shipmentRepository.save(shipment);
        buyerNotifier.notifyBuyer(order.getBuyerId(), "Shipment created",
                "Shipment " + trackingNumber.getValue() + " was created for order " + orderId + ".");
        return saved;
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
