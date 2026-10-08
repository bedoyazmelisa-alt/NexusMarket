package application.infrastructure.adapter.web.dto;

import application.domain.enums.ProductStatus;
import application.domain.enums.ProductType;
import application.domain.model.Product;

/** Product view returned by the API (primitives only — no domain types). */
public record ProductResponse(Long id, Long sellerId, String name, String description,
                              ProductType productType, ProductStatus status, MoneyResponse basePrice) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getSellerId(), product.getName(),
                product.getDescription(), product.getProductType(), product.getStatus(),
                MoneyResponse.from(product.getBasePrice()));
    }
}
