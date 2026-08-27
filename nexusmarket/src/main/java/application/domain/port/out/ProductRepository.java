package application.domain.port.out;

import application.domain.model.Product;
import application.domain.valueobject.ProductCode;

import java.util.List;
import java.util.Optional;

/**
 * Output port for persisting and retrieving {@link Product} aggregates.
 */
public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findBySellerId(Long sellerId);

    boolean existsByCode(ProductCode code);
}