package application.domain.service;

import application.domain.exception.InsufficientInventoryException;
import application.domain.exception.InvalidOrderStateException;
import application.domain.model.Cart;
import application.domain.model.CartItem;
import application.domain.model.Inventory;
import application.domain.model.Order;
import application.domain.port.out.CartRepository;
import application.domain.port.out.InventoryRepository;
import application.domain.port.out.OrderRepository;

import java.util.List;

/**
 * Domain service that coordinates the creation of a formal order from a cart:
 * validates the buyer, the cart, product availability, reserves inventory,
 * creates the order, calculates totals and persists it through output ports.
 */
public class OrderCreationService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;

    public OrderCreationService(CartRepository cartRepository,
                                OrderRepository orderRepository,
                                InventoryRepository inventoryRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Order createOrder(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new InvalidOrderStateException("Cart does not exist"));
        if (cart.isEmpty()) {
            throw new InvalidOrderStateException("Cannot create an order from an empty cart");
        }

        for (CartItem item : cart.getItems()) {
            reserveInventory(item.getProductId(), item.getQuantity());
        }

        Order order = Order.create(cart.getBuyerId());
        for (CartItem item : cart.getItems()) {
            order.addItem(item.getProductId(), item.getQuantity(), item.getUnitPrice());
        }
        order.calculateTotal();

        Order saved = orderRepository.save(order);
        cart.checkout();
        cartRepository.save(cart);
        return saved;
    }

    private void reserveInventory(Long productId, int quantity) {
        List<Inventory> inventories = inventoryRepository.findByProductId(productId);
        for (Inventory inventory : inventories) {
            if (inventory.getAvailableQuantity() >= quantity) {
                inventory.reserve(quantity, "Order reservation");
                inventoryRepository.save(inventory);
                return;
            }
        }
        throw new InsufficientInventoryException(
                "No warehouse has enough available inventory for product " + productId);
    }
}