package application.domain.model;

import application.domain.enums.SellerStatus;
import lombok.Getter;

/**
 * A business participant responsible for commercializing products through the
 * marketplace. Sellers cannot self-register (RG-04); they must be incorporated
 * by an Administrator (RG-05).
 */
@Getter
public class Seller {

    private Long id;
    private Long userId;
    private String businessInformation;
    private SellerStatus status;

    private Seller() {
    }

    public static Seller create(Long userId, String businessInformation) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null");
        }
        if (businessInformation == null || businessInformation.isBlank()) {
            throw new IllegalArgumentException("Business information must not be blank");
        }
        Seller seller = new Seller();
        seller.userId = userId;
        seller.businessInformation = businessInformation.trim();
        seller.status = SellerStatus.ACTIVE;
        return seller;
    }

    /**
     * Rebuilds a seller from persisted data. Unlike {@link #create}, it preserves
     * the stored state exactly (status, id) and skips creation-time defaults;
     * used only by persistence adapters to rehydrate the domain.
     */
    public static Seller reconstitute(Long id, Long userId, String businessInformation,
                                      SellerStatus status) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null");
        }
        if (businessInformation == null || businessInformation.isBlank()) {
            throw new IllegalArgumentException("Business information must not be blank");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
        Seller seller = new Seller();
        seller.id = id;
        seller.userId = userId;
        seller.businessInformation = businessInformation.trim();
        seller.status = status;
        return seller;
    }

    public void activate() {
        this.status = SellerStatus.ACTIVE;
    }

    public void suspend() {
        this.status = SellerStatus.SUSPENDED;
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}