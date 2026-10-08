package application.service;

import application.domain.enums.ProductType;
import application.domain.enums.WarehouseType;
import application.domain.exception.DamagedInventoryException;
import application.domain.exception.InsufficientInventoryException;
import application.domain.exception.InvalidInventoryMovementException;
import application.domain.model.Inventory;
import application.domain.model.Product;
import application.domain.model.Warehouse;
import application.domain.valueobject.Address;
import application.domain.valueobject.Money;
import application.infrastructure.adapter.inmemory.InMemoryInventoryRepository;
import application.infrastructure.adapter.inmemory.InMemoryProductRepository;
import application.infrastructure.adapter.inmemory.InMemoryWarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Domain tests for the inventory rules (RG-06, RG-07, RG-08, RG-09):
 * inventory can never become negative, damaged stock cannot be reserved and
 * every movement goes through the domain. No infrastructure required.
 */
class InventoryServiceTest {

    private InventoryService inventoryService;
    private InMemoryInventoryRepository inventoryRepository;
    private Long productId;
    private Long warehouseId;

    @BeforeEach
    void setUp() {
        inventoryRepository = new InMemoryInventoryRepository();
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        InMemoryWarehouseRepository warehouseRepository = new InMemoryWarehouseRepository();
        inventoryService = new InventoryService(
                inventoryRepository, productRepository, warehouseRepository);

        Product product = productRepository.save(Product.create(
                1L, "Notebook", "14-inch laptop", ProductType.PHYSICAL,
                Money.of("999.99", "USD")));
        productId = product.getId();

        Warehouse warehouse = warehouseRepository.save(Warehouse.create(
                "Main Warehouse",
                Address.of("Street 1", "Buenos Aires", "CABA", "1000", "Argentina"),
                WarehouseType.MARKETPLACE));
        warehouseId = warehouse.getId();
    }

    @Test
    void shouldNotAllowNegativeInventory() {
        inventoryService.registerEntry(productId, warehouseId, 10, "Initial stock");

        // A negative entry must be rejected by the domain (RG-07).
        InvalidInventoryMovementException negativeEntry = assertThrows(
                InvalidInventoryMovementException.class,
                () -> inventoryService.registerEntry(productId, warehouseId, -5, "Bad entry"));
        assertTrue(negativeEntry.getMessage().contains("positive"));

        // Adjusting to a negative quantity is rejected as well.
        InvalidInventoryMovementException negativeAdjust = assertThrows(
                InvalidInventoryMovementException.class,
                () -> inventoryService.adjust(productId, warehouseId, -1, "Bad adjustment"));
        assertTrue(negativeAdjust.getMessage().contains("negative"));

        // Neither attempt changed the stored quantity.
        Inventory stored = currentInventory();
        assertEquals(10, stored.getAvailableQuantity());
        assertEquals(0, stored.getReservedQuantity());
    }

    @Test
    void shouldNotReserveDamagedInventory() {
        inventoryService.registerEntry(productId, warehouseId, 10, "Initial stock");
        Inventory inventory = currentInventory();
        inventory.registerDamage(10, "Flood damage");
        assertEquals(0, inventory.getAvailableQuantity());
        assertEquals(10, inventory.getDamagedQuantity());

        // Damaged stock cannot be reserved (RG-09).
        assertThrows(DamagedInventoryException.class,
                () -> inventoryService.reserve(productId, warehouseId, 1, "Order reservation"));

        assertEquals(0, inventory.getAvailableQuantity());
        assertEquals(0, inventory.getReservedQuantity());
    }

    @Test
    void shouldNotReserveMoreThanAvailableInventory() {
        inventoryService.registerEntry(productId, warehouseId, 3, "Initial stock");

        // Healthy but insufficient stock reports the insufficiency (RG-08),
        // not the damaged-stock rule.
        InsufficientInventoryException exception = assertThrows(
                InsufficientInventoryException.class,
                () -> inventoryService.reserve(productId, warehouseId, 5, "Order reservation"));
        assertTrue(exception.getMessage().contains("requested 5"));
        assertEquals(3, currentInventory().getAvailableQuantity());
    }

    @Test
    void shouldReserveAvailableInventory() {
        inventoryService.registerEntry(productId, warehouseId, 10, "Initial stock");

        inventoryService.reserve(productId, warehouseId, 4, "Order reservation");

        Inventory inventory = currentInventory();
        assertEquals(6, inventory.getAvailableQuantity());
        assertEquals(4, inventory.getReservedQuantity());
    }

    private Inventory currentInventory() {
        return inventoryRepository.findByProductAndWarehouse(productId, warehouseId)
                .orElseThrow(() -> new AssertionError("Inventory must exist for the seeded product"));
    }
}
