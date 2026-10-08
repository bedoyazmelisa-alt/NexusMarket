package application.service;

import application.domain.enums.ProductStatus;
import application.domain.enums.ProductType;
import application.domain.exception.InvalidProductStateException;
import application.domain.model.Product;
import application.domain.model.Seller;
import application.domain.valueobject.Money;
import application.infrastructure.adapter.inmemory.InMemoryProductRepository;
import application.infrastructure.adapter.inmemory.InMemorySellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Domain tests for the product status lifecycle: a product must always be in
 * a valid status and a discontinued product cannot transition anymore.
 */
class ProductServiceTest {

    private ProductService productService;
    private InMemoryProductRepository productRepository;
    private Long sellerId;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        InMemorySellerRepository sellerRepository = new InMemorySellerRepository();
        productService = new ProductService(productRepository, sellerRepository);

        Seller seller = sellerRepository.save(
                Seller.create(1L, "ACME Inc."));
        sellerId = seller.getId();
    }

    @Test
    void shouldRequireValidProductStatus() {
        Product product = productService.createProduct(sellerId, "Notebook",
                "14-inch laptop", ProductType.PHYSICAL, Money.of("999.99", "USD"));
        assertEquals(ProductStatus.SUSPENDED, product.getStatus());

        // SUSPENDED -> PUBLISHED is a valid transition.
        productService.publishProduct(product.getId());
        assertEquals(ProductStatus.PUBLISHED, currentProduct(product.getId()).getStatus());

        // DISCONTINUED is terminal: publishing again is not a valid status transition.
        product.discontinue();
        InvalidProductStateException publishException = assertThrows(
                InvalidProductStateException.class,
                () -> productService.publishProduct(product.getId()));
        assertTrue(publishException.getMessage().contains("discontinued"));

        // Suspending a discontinued product is invalid as well.
        assertThrows(InvalidProductStateException.class, product::suspend);

        assertEquals(ProductStatus.DISCONTINUED, currentProduct(product.getId()).getStatus());
    }

    private Product currentProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new AssertionError("Product must exist: " + productId));
    }
}
