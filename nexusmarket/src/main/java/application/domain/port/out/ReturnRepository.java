package application.domain.port.out;

import application.domain.model.Return;

import java.util.List;
import java.util.Optional;

/**
 * Output port for persisting and retrieving {@link Return} aggregates.
 */
public interface ReturnRepository {

    Return save(Return returnRequest);

    Optional<Return> findById(Long id);

    List<Return> findByOrderId(Long orderId);
}