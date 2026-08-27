package application.domain.service;

import application.domain.enums.ReturnStatus;
import application.domain.exception.InvalidRefundException;
import application.domain.model.Refund;
import application.domain.model.Return;
import application.domain.port.out.RefundRepository;
import application.domain.port.out.ReturnRepository;
import application.domain.valueobject.Money;

/**
 * Domain service that processes a refund associated with an approved return.
 */
public class RefundProcessingService {

    private final ReturnRepository returnRepository;
    private final RefundRepository refundRepository;

    public RefundProcessingService(ReturnRepository returnRepository, RefundRepository refundRepository) {
        this.returnRepository = returnRepository;
        this.refundRepository = refundRepository;
    }

    public Refund process(Long returnId, Money amount) {
        if (returnId == null || amount == null) {
            throw new InvalidRefundException("Return id and amount are required");
        }
        Return returnRequest = returnRepository.findById(returnId)
                .orElseThrow(() -> new InvalidRefundException(
                        "Return with id " + returnId + " does not exist"));
        if (returnRequest.getStatus() != ReturnStatus.APPROVED) {
            throw new InvalidRefundException(
                    "Refunds can only be processed for approved returns");
        }

        Refund refund = Refund.create(returnId, amount);
        refund.markProcessed();
        returnRequest.markProcessed();
        returnRepository.save(returnRequest);
        return refundRepository.save(refund);
    }
}