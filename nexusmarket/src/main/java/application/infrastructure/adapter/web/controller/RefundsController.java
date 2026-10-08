package application.infrastructure.adapter.web.controller;

import application.domain.model.Refund;
import application.domain.port.in.ProcessRefundUseCase;
import application.infrastructure.adapter.web.dto.ProcessRefundRequest;
import application.infrastructure.adapter.web.dto.RefundResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes {@link ProcessRefundUseCase} over HTTP. */
@RestController
@RequestMapping("/api/refunds")
public class RefundsController {

    private final ProcessRefundUseCase processRefund;

    public RefundsController(ProcessRefundUseCase processRefund) {
        this.processRefund = processRefund;
    }

    @PostMapping
    public ResponseEntity<RefundResponse> process(@RequestBody ProcessRefundRequest request) {
        Refund refund = processRefund.processRefund(request.returnId(), request.amount().toMoney());
        return ResponseEntity.status(HttpStatus.CREATED).body(RefundResponse.from(refund));
    }
}
