package application.infrastructure.adapter.web.controller;

import application.domain.model.Cart;
import application.domain.port.in.CreateCartUseCase;
import application.infrastructure.adapter.web.dto.CartResponse;
import application.infrastructure.adapter.web.dto.CreateCartRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes {@link CreateCartUseCase} over HTTP. */
@RestController
@RequestMapping("/api/carts")
public class CartsController {

    private final CreateCartUseCase createCart;

    public CartsController(CreateCartUseCase createCart) {
        this.createCart = createCart;
    }

    @PostMapping
    public ResponseEntity<CartResponse> create(@RequestBody CreateCartRequest request) {
        Cart cart = createCart.createCart(request.buyerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(CartResponse.from(cart));
    }
}
