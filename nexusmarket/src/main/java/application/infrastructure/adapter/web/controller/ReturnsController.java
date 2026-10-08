package application.infrastructure.adapter.web.controller;

import application.domain.model.Return;
import application.domain.port.in.CreateReturnUseCase;
import application.infrastructure.adapter.web.dto.CreateReturnRequest;
import application.infrastructure.adapter.web.dto.ReturnResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes {@link CreateReturnUseCase} over HTTP. */
@RestController
@RequestMapping("/api/returns")
public class ReturnsController {

    private final CreateReturnUseCase createReturn;

    public ReturnsController(CreateReturnUseCase createReturn) {
        this.createReturn = createReturn;
    }

    @PostMapping
    public ResponseEntity<ReturnResponse> create(@RequestBody CreateReturnRequest request) {
        Return created = createReturn.createReturn(request.orderId(), request.reason());
        return ResponseEntity.status(HttpStatus.CREATED).body(ReturnResponse.from(created));
    }
}
