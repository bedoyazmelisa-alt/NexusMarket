package application.service;

import application.domain.enums.ProductType;
import application.domain.model.Product;
import application.domain.port.in.CreateProductUseCase;
import application.domain.port.in.PublishProductUseCase;
import application.domain.port.out.ProductRepository;
import application.domain.port.out.SellerRepository;
import application.domain.valueobject.Money;

/**
 * Application service for the product catalog. Implements
 * {@link CreateProductUseCase} and {@link PublishProductUseCase}: products are
 * always owned by an existing seller and start suspended until they are
 * published.
 */
public class ProductService implements CreateProductUseCase, PublishProductUseCase {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public ProductService(ProductRepository productRepository, SellerRepository sellerRepository) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public Product createProduct(Long sellerId, String name, String description,
                                 ProductType productType, Money basePrice) {
        Product product = Product.create(sellerId, name, description, productType, basePrice);
        sellerRepository.findById(product.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("Seller does not exist"));
        return productRepository.save(product);
    }

    @Override
    public Product publishProduct(Long productId) {
        Product product = requireProduct(productId);
        product.publish();
        return productRepository.save(product);
    }

    private Product requireProduct(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("ProductId must not be null");
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product with id " + productId + " does not exist"));
    }
}
