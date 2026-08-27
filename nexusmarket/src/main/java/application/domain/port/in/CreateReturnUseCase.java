package application.domain.port.in;

import application.domain.model.Return;

/**
 * Input port: creates a return request for an order.
 */
public interface CreateReturnUseCase {

    Return createReturn(Long orderId, String reason);
}