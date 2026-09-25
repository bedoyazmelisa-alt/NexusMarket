package application.infrastructure.adapter.inmemory;

import application.domain.model.Product;
import application.domain.port.out.ProductRepository;
import application.domain.valueobject.ProductCode;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * In-memory {@link ProductRepository}. Stand-in until the MySQL adapter
 * exists.
 */
@Repository
public class InMemoryProductRepository extends InMemoryRepository<Product> implements ProductRepository {

    public InMemoryProductRepository() {
        super(Product::getId, Product::assignId);
    }

    @Override
    public Product save(Product product) {
        return persist(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return row(id);
    }

    @Override
    public List<Product> findBySellerId(Long sellerId) {
        if (sellerId == null) {
            return List.of();
        }
        return all().stream().filter(product -> sellerId.equals(product.getSellerId())).toList();
    }

    @Override
    public boolean existsByCode(ProductCode code) {
        // TODO: the Product entity does not model its ProductCode yet, so a
        // product can never match a code. Enable once the field exists.
        return false;
    }
}
