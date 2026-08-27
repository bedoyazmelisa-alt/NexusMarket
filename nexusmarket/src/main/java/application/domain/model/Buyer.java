package application.domain.model;

import application.domain.enums.BuyerStatus;
import application.domain.valueobject.Address;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A customer who purchases products through NexusMarket. A buyer must have a
 * primary address; additional addresses are optional.
 */
@Getter
public class Buyer {

    private Long id;
    private Long userId;
    private Address primaryAddress;
    private final List<Address> additionalAddresses = new ArrayList<>();
    private BuyerStatus commercialStatus;

    private Buyer() {
    }

    public static Buyer create(Long userId, Address primaryAddress) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null");
        }
        if (primaryAddress == null) {
            throw new IllegalArgumentException("Primary address must not be null");
        }
        Buyer buyer = new Buyer();
        buyer.userId = userId;
        buyer.primaryAddress = primaryAddress;
        buyer.commercialStatus = BuyerStatus.ACTIVE;
        return buyer;
    }

    public void addAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address must not be null");
        }
        if (additionalAddresses.contains(address) || primaryAddress.equals(address)) {
            throw new IllegalArgumentException("Address already exists");
        }
        this.additionalAddresses.add(address);
    }

    public void removeAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address must not be null");
        }
        if (!additionalAddresses.remove(address)) {
            throw new IllegalArgumentException("Address does not exist");
        }
    }

    public void setPrimaryAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address must not be null");
        }
        this.additionalAddresses.remove(address);
        this.primaryAddress = address;
    }

    public void activate() {
        this.commercialStatus = BuyerStatus.ACTIVE;
    }

    public void suspend() {
        this.commercialStatus = BuyerStatus.SUSPENDED;
    }

    public List<Address> getAdditionalAddresses() {
        return Collections.unmodifiableList(additionalAddresses);
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}