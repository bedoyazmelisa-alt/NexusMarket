package application.infrastructure.adapter.web.controller;

import application.domain.model.Seller;
import application.domain.model.User;
import application.domain.port.in.RegisterSellerUseCase;
import application.domain.port.out.UserRepository;
import application.infrastructure.adapter.web.dto.RegisterSellerRequest;
import application.infrastructure.adapter.web.dto.SellerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes {@link RegisterSellerUseCase} over HTTP. */
@RestController
@RequestMapping("/api/sellers")
public class SellersController {

    private final RegisterSellerUseCase registerSeller;
    private final UserRepository userRepository;

    public SellersController(RegisterSellerUseCase registerSeller, UserRepository userRepository) {
        this.registerSeller = registerSeller;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<SellerResponse> register(@RequestBody RegisterSellerRequest request) {
        if (request.actorId() == null) {
            throw new IllegalArgumentException("Unknown actor id");
        }
        User actor = userRepository.findById(request.actorId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown actor id"));
        Seller seller = registerSeller.registerSeller(actor, request.userId(),
                request.businessInformation());
        return ResponseEntity.status(HttpStatus.CREATED).body(SellerResponse.from(seller));
    }
}
