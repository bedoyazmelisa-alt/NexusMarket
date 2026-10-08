package application.infrastructure.adapter.jpa;

import application.domain.model.Invoice;
import application.domain.port.out.InvoiceRepository;
import application.domain.valueobject.InvoiceNumber;
import application.domain.valueobject.Money;
import application.infrastructure.adapter.jpa.entity.InvoiceEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MySQL/JPA implementation of {@link InvoiceRepository}. Translates between the
 * persistence model ({@link InvoiceEntity}) and the domain model; no business
 * rules live here.
 */
@Repository
@Profile("!in-memory")
public class JpaInvoiceRepository implements InvoiceRepository {

    private final InvoiceJpaRepository jpaRepository;

    public JpaInvoiceRepository(InvoiceJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceEntity entity = toEntity(invoice);
        InvoiceEntity saved = jpaRepository.save(entity);
        if (invoice.getId() == null) {
            invoice.assignId(saved.getId());
        }
        return invoice;
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Invoice> findByOrderId(Long orderId) {
        if (orderId == null) {
            return Optional.empty();
        }
        return jpaRepository.findByOrderId(orderId).map(this::toDomain);
    }

    private InvoiceEntity toEntity(Invoice invoice) {
        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(invoice.getId());
        entity.setOrderId(invoice.getOrderId());
        entity.setInvoiceNumber(invoice.getInvoiceNumber().getValue());
        entity.setIssueDate(invoice.getIssueDate());
        entity.setSubtotalAmount(invoice.getSubtotal().getAmount());
        entity.setSubtotalCurrency(invoice.getSubtotal().getCurrency());
        entity.setTaxesAmount(invoice.getTaxes().getAmount());
        entity.setTaxesCurrency(invoice.getTaxes().getCurrency());
        entity.setTotalAmount(invoice.getTotal().getAmount());
        entity.setTotalCurrency(invoice.getTotal().getCurrency());
        entity.setStatus(invoice.getStatus());
        return entity;
    }

    private Invoice toDomain(InvoiceEntity entity) {
        return Invoice.reconstitute(
                entity.getId(),
                entity.getOrderId(),
                InvoiceNumber.of(entity.getInvoiceNumber()),
                entity.getIssueDate(),
                Money.of(entity.getSubtotalAmount(), entity.getSubtotalCurrency()),
                Money.of(entity.getTaxesAmount(), entity.getTaxesCurrency()),
                Money.of(entity.getTotalAmount(), entity.getTotalCurrency()),
                entity.getStatus());
    }
}
