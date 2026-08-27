package application.domain.port.out;

import application.domain.model.Invoice;

import java.util.Optional;

/**
 * Output port for persisting and retrieving {@link Invoice} entities.
 */
public interface InvoiceRepository {

    Invoice save(Invoice invoice);

    Optional<Invoice> findById(Long id);

    Optional<Invoice> findByOrderId(Long orderId);
}