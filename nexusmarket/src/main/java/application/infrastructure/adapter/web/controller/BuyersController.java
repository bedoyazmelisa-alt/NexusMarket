package application.infrastructure.adapter.web.controller;

import application.domain.model.Buyer;
import application.domain.port.in.RegisterBuyerUseCase;
import application.domain.valueobject.Address;
import application.infrastructure.adapter.web.dto.BuyerResponse;
import application.infrastructure.adapter.web.dto.RegisterBuyerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes {@link RegisterBuyerUseCase} over HTTP. */
@RestController
@RequestMapping("/api/buyers")
public class BuyersController {

    private final RegisterBuyerUseCase registerBuyer;

    public BuyersController(RegisterBuyerUseCase registerBuyer) {
        this.registerBuyer = registerBuyer;
    }

    @PostMapping
    public ResponseEntity<BuyerResponse> register(@RequestBody RegisterBuyerRequest request) {
        Address primaryAddress = request.primaryAddress() == null
                ? null : request.primaryAddress().toDomain();
        Buyer buyer = registerBuyer.registerBuyer(request.userId(), primaryAddress);
        return ResponseEntity.status(HttpStatus.CREATED).body(BuyerResponse.from(buyer));
    }
}
