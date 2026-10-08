package application.infrastructure.adapter.web.dto;

import application.domain.enums.ProductType;

/** Request to create a product owned by a seller. */
public record CreateProductRequest(Long sellerId, String name, String description,
                                   ProductType productType, MoneyRequest basePrice) {
}
