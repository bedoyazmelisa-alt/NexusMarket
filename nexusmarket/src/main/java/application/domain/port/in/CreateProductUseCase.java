package application.domain.port.in;

import application.domain.enums.ProductType;
import application.domain.model.Product;
import application.domain.valueobject.Money;

/**
 * Input port: creates a product owned by a seller.
 */
public interface CreateProductUseCase {

    Product createProduct(Long sellerId, String name, String description,
                          ProductType productType, Money basePrice);
}