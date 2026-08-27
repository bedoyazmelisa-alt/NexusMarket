package application.domain.port.out;

import application.domain.model.Seller;

import java.util.Optional;

/**
 * Output port for persisting and retrieving {@link Seller} aggregates.
 */
public interface SellerRepository {

    Seller save(Seller seller);

    Optional<Seller> findById(Long id);

    Optional<Seller> findByUserId(Long userId);
}