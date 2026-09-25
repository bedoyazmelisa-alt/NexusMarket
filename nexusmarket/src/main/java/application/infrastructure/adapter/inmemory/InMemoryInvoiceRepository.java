package application.infrastructure.adapter.inmemory;

import application.domain.model.Invoice;
import application.domain.port.out.InvoiceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * In-memory {@link InvoiceRepository}. Stand-in until the MySQL adapter
 * exists. No application service consumes invoices yet; the invoicing rules
 * must be defined by requirements first.
 */
@Repository
public class InMemoryInvoiceRepository extends InMemoryRepository<Invoice> implements InvoiceRepository {

    public InMemoryInvoiceRepository() {
        super(Invoice::getId, Invoice::assignId);
    }

    @Override
    public Invoice save(Invoice invoice) {
        return persist(invoice);
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        return row(id);
    }

    @Override
    public Optional<Invoice> findByOrderId(Long orderId) {
        if (orderId == null) {
            return Optional.empty();
        }
        return all().stream().filter(invoice -> orderId.equals(invoice.getOrderId())).findFirst();
    }
}
