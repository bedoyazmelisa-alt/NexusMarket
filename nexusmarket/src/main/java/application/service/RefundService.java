package application.service;

import application.domain.model.Refund;
import application.domain.port.in.ProcessRefundUseCase;
import application.domain.port.out.RefundRepository;
import application.domain.port.out.ReturnRepository;
import application.domain.service.RefundProcessingService;
import application.domain.valueobject.Money;

/**
 * Application service for refunds. Implements {@link ProcessRefundUseCase} by
 * delegating to the {@link RefundProcessingService} domain service, which only
 * accepts refunds for approved returns.
 */
public class RefundService implements ProcessRefundUseCase {

    private final RefundProcessingService refundProcessingService;

    public RefundService(ReturnRepository returnRepository, RefundRepository refundRepository) {
        this.refundProcessingService = new RefundProcessingService(returnRepository, refundRepository);
    }

    @Override
    public Refund processRefund(Long returnId, Money amount) {
        return refundProcessingService.process(returnId, amount);
    }
}
