package application.infrastructure.adapter.web.controller;

import application.domain.model.Shipment;
import application.domain.port.in.CreateShipmentUseCase;
import application.domain.valueobject.TrackingNumber;
import application.infrastructure.adapter.web.dto.CreateShipmentRequest;
import application.infrastructure.adapter.web.dto.ShipmentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes {@link CreateShipmentUseCase} over HTTP. */
@RestController
@RequestMapping("/api/shipments")
public class ShipmentsController {

    private final CreateShipmentUseCase createShipment;

    public ShipmentsController(CreateShipmentUseCase createShipment) {
        this.createShipment = createShipment;
    }

    @PostMapping
    public ResponseEntity<ShipmentResponse> create(@RequestBody CreateShipmentRequest request) {
        Shipment shipment = createShipment.createShipment(request.orderId(), request.warehouseId(),
                TrackingNumber.of(request.trackingNumber()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ShipmentResponse.from(shipment));
    }
}
