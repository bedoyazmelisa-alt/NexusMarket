package application.infrastructure.adapter.web.controller;

import application.domain.model.Product;
import application.domain.port.in.CreateProductUseCase;
import application.domain.port.in.PublishProductUseCase;
import application.infrastructure.adapter.web.dto.CreateProductRequest;
import application.infrastructure.adapter.web.dto.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes {@link CreateProductUseCase} and {@link PublishProductUseCase} over HTTP. */
@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final CreateProductUseCase createProduct;
    private final PublishProductUseCase publishProduct;

    public ProductsController(CreateProductUseCase createProduct, PublishProductUseCase publishProduct) {
        this.createProduct = createProduct;
        this.publishProduct = publishProduct;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody CreateProductRequest request) {
        Product product = createProduct.createProduct(request.sellerId(), request.name(),
                request.description(), request.productType(),
                request.basePrice() == null ? null : request.basePrice().toMoney());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.from(product));
    }

    @PatchMapping("/{productId}/publish")
    public ResponseEntity<ProductResponse> publish(@PathVariable Long productId) {
        Product product = publishProduct.publishProduct(productId);
        return ResponseEntity.ok(ProductResponse.from(product));
    }
}
