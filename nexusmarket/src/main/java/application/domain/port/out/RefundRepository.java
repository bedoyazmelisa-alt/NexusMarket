package application.domain.port.out;

import application.domain.model.Refund;

import java.util.Optional;

/**
 * Output port for persisting and retrieving {@link Refund} entities.
 */
public interface RefundRepository {

    Refund save(Refund refund);

    Optional<Refund> findById(Long id);

    Optional<Refund> findByReturnId(Long returnId);
}