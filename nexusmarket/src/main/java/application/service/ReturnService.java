package application.service;

import application.domain.exception.InvalidOrderStateException;
import application.domain.model.Return;
import application.domain.port.in.CreateReturnUseCase;
import application.domain.port.out.OrderRepository;
import application.domain.port.out.ReturnRepository;

/**
 * Application service for returns. Implements {@link CreateReturnUseCase}:
 * the return must reference an existing order and must carry a reason.
 *
 * <p>Eligibility and approval rules are intentionally not applied here
 * because they are not defined in the functional specification yet.</p>
 */
public class ReturnService implements CreateReturnUseCase {

    private final ReturnRepository returnRepository;
    private final OrderRepository orderRepository;

    public ReturnService(ReturnRepository returnRepository, OrderRepository orderRepository) {
        this.returnRepository = returnRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Return createReturn(Long orderId, String reason) {
        Return returnRequest = Return.create(orderId, reason);
        orderRepository.findById(returnRequest.getOrderId())
                .orElseThrow(() -> new InvalidOrderStateException(
                        "Order with id " + returnRequest.getOrderId() + " does not exist"));
        return returnRepository.save(returnRequest);
    }
}
