package application.infrastructure.adapter.jpa;

import application.domain.model.Product;
import application.domain.model.ProductVariant;
import application.domain.port.out.ProductRepository;
import application.domain.valueobject.Money;
import application.domain.valueobject.ProductCode;
import application.infrastructure.adapter.jpa.entity.ProductEntity;
import application.infrastructure.adapter.jpa.entity.ProductVariantEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * MySQL/JPA implementation of {@link ProductRepository}. Translates between the
 * persistence model ({@link ProductEntity}) and the domain model; no business
 * rules live here.
 */
@Repository
@Profile("!in-memory")
public class JpaProductRepository implements ProductRepository {

    private final ProductJpaRepository jpaRepository;

    public JpaProductRepository(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity saved = jpaRepository.save(entity);
        if (product.getId() == null) {
            product.assignId(saved.getId());
        }
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Product> findBySellerId(Long sellerId) {
        if (sellerId == null) {
            return List.of();
        }
        return jpaRepository.findBySellerId(sellerId).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsByCode(ProductCode code) {
        // TODO: the Product entity does not model its ProductCode yet, so a
        // product can never match a code. Enable once the field exists.
        return false;
    }

    private ProductEntity toEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setSellerId(product.getSellerId());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setProductType(product.getProductType());
        entity.setStatus(product.getStatus());
        entity.setBasePriceAmount(product.getBasePrice().getAmount());
        entity.setBasePriceCurrency(product.getBasePrice().getCurrency());
        List<ProductVariantEntity> variantEntities = new ArrayList<>();
        int position = 0;
        for (ProductVariant variant : product.getVariants()) {
            ProductVariantEntity variantEntity = toVariantEntity(variant);
            variantEntity.setPosition(position++);
            variantEntities.add(variantEntity);
        }
        entity.setVariants(variantEntities);
        return entity;
    }

    private Product toDomain(ProductEntity entity) {
        List<ProductVariant> variants = entity.getVariants() == null
                ? List.of()
                : entity.getVariants().stream()
                        .map(embeddable -> toVariantDomain(embeddable, entity.getId()))
                        .toList();
        return Product.reconstitute(
                entity.getId(),
                entity.getSellerId(),
                entity.getName(),
                entity.getDescription(),
                entity.getProductType(),
                entity.getStatus(),
                Money.of(entity.getBasePriceAmount(), entity.getBasePriceCurrency()),
                variants);
    }

    private ProductVariantEntity toVariantEntity(ProductVariant variant) {
        ProductVariantEntity variantEntity = new ProductVariantEntity();
        variantEntity.setId(variant.getId());
        variantEntity.setName(variant.getName());
        variantEntity.setAttributes(new LinkedHashMap<>(variant.getAttributes()));
        variantEntity.setPriceAmount(variant.getPrice().getAmount());
        variantEntity.setPriceCurrency(variant.getPrice().getCurrency());
        variantEntity.setStatus(variant.getStatus());
        return variantEntity;
    }

    private ProductVariant toVariantDomain(ProductVariantEntity variantEntity, Long productId) {
        return ProductVariant.reconstitute(
                variantEntity.getId(),
                productId,
                variantEntity.getName(),
                variantEntity.getAttributes(),
                Money.of(variantEntity.getPriceAmount(), variantEntity.getPriceCurrency()),
                variantEntity.getStatus());
    }
}
