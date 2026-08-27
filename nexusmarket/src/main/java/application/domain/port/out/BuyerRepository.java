package application.domain.port.out;

import application.domain.model.Buyer;

import java.util.Optional;

/**
 * Output port for persisting and retrieving {@link Buyer} aggregates.
 */
public interface BuyerRepository {

    Buyer save(Buyer buyer);

    Optional<Buyer> findById(Long id);

    Optional<Buyer> findByUserId(Long userId);
}