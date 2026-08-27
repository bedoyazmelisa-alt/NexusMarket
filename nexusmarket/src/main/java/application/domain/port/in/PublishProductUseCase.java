package application.domain.port.in;

import application.domain.model.Product;

/**
 * Input port: publishes a product following marketplace publication rules.
 */
public interface PublishProductUseCase {

    Product publishProduct(Long productId);
}