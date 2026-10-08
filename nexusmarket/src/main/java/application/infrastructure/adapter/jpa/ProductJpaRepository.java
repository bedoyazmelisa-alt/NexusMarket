package application.infrastructure.adapter.jpa;

import application.infrastructure.adapter.jpa.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for {@link ProductEntity}. Technical query layer only;
 * domain logic never sees this interface.
 */
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findBySellerId(Long sellerId);
}
