package application.infrastructure.config;

import application.domain.port.out.BuyerRepository;
import application.domain.port.out.CartRepository;
import application.domain.port.out.InventoryRepository;
import application.domain.port.out.NotificationService;
import application.domain.port.out.OrderRepository;
import application.domain.port.out.PaymentService;
import application.domain.port.out.ProductRepository;
import application.domain.port.out.RefundRepository;
import application.domain.port.out.ReturnRepository;
import application.domain.port.out.SellerRepository;
import application.domain.port.out.ShipmentRepository;
import application.domain.port.out.UserRepository;
import application.domain.port.out.WarehouseRepository;
import application.service.BuyerService;
import application.service.CartService;
import application.service.InventoryService;
import application.service.OrderService;
import application.service.ProductService;
import application.service.RefundService;
import application.service.ReportService;
import application.service.ReturnService;
import application.service.SellerService;
import application.service.ShipmentService;
import application.service.UserService;
import application.service.WarehouseService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Composition root of the application. Wires every application service to the
 * output ports it needs; the adapters implementing those ports are discovered
 * by component scanning.
 *
 * <p>The services themselves stay framework-free (no Spring annotations), so
 * everything that knows about Spring lives here and in the adapters.</p>
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public BuyerService buyerService(BuyerRepository buyerRepository, UserRepository userRepository) {
        return new BuyerService(buyerRepository, userRepository);
    }

    @Bean
    public SellerService sellerService(SellerRepository sellerRepository, UserRepository userRepository) {
        return new SellerService(sellerRepository, userRepository);
    }

    @Bean
    public WarehouseService warehouseService(WarehouseRepository warehouseRepository) {
        return new WarehouseService(warehouseRepository);
    }

    @Bean
    public ProductService productService(ProductRepository productRepository, SellerRepository sellerRepository) {
        return new ProductService(productRepository, sellerRepository);
    }

    @Bean
    public InventoryService inventoryService(InventoryRepository inventoryRepository,
                                             ProductRepository productRepository,
                                             WarehouseRepository warehouseRepository) {
        return new InventoryService(inventoryRepository, productRepository, warehouseRepository);
    }

    @Bean
    public CartService cartService(CartRepository cartRepository, BuyerRepository buyerRepository) {
        return new CartService(cartRepository, buyerRepository);
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository,
                                     CartRepository cartRepository,
                                     InventoryRepository inventoryRepository,
                                     PaymentService paymentService,
                                     BuyerRepository buyerRepository,
                                     UserRepository userRepository,
                                     NotificationService notificationService) {
        return new OrderService(orderRepository, cartRepository, inventoryRepository, paymentService,
                buyerRepository, userRepository, notificationService);
    }

    @Bean
    public ShipmentService shipmentService(ShipmentRepository shipmentRepository,
                                           OrderRepository orderRepository,
                                           WarehouseRepository warehouseRepository,
                                           BuyerRepository buyerRepository,
                                           UserRepository userRepository,
                                           NotificationService notificationService) {
        return new ShipmentService(shipmentRepository, orderRepository, warehouseRepository,
                buyerRepository, userRepository, notificationService);
    }

    @Bean
    public ReturnService returnService(ReturnRepository returnRepository, OrderRepository orderRepository) {
        return new ReturnService(returnRepository, orderRepository);
    }

    @Bean
    public RefundService refundService(ReturnRepository returnRepository, RefundRepository refundRepository) {
        return new RefundService(returnRepository, refundRepository);
    }

    @Bean
    public ReportService reportService(OrderRepository orderRepository) {
        return new ReportService(orderRepository);
    }
}
