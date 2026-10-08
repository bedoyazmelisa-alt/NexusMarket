package application.infrastructure.adapter.jpa;

import application.domain.model.Buyer;
import application.domain.port.out.BuyerRepository;
import application.domain.valueobject.Address;
import application.infrastructure.adapter.jpa.entity.AddressEmbeddable;
import application.infrastructure.adapter.jpa.entity.BuyerEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MySQL/JPA implementation of {@link BuyerRepository}. Translates between the
 * persistence model ({@link BuyerEntity}) and the domain model; no business
 * rules live here.
 */
@Repository
@Profile("!in-memory")
public class JpaBuyerRepository implements BuyerRepository {

    private final BuyerJpaRepository jpaRepository;

    public JpaBuyerRepository(BuyerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Buyer save(Buyer buyer) {
        BuyerEntity entity = toEntity(buyer);
        BuyerEntity saved = jpaRepository.save(entity);
        if (buyer.getId() == null) {
            buyer.assignId(saved.getId());
        }
        return buyer;
    }

    @Override
    public Optional<Buyer> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Buyer> findByUserId(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }
        return jpaRepository.findByUserId(userId).map(this::toDomain);
    }

    private BuyerEntity toEntity(Buyer buyer) {
        BuyerEntity entity = new BuyerEntity();
        entity.setId(buyer.getId());
        entity.setUserId(buyer.getUserId());
        entity.setStreet(buyer.getPrimaryAddress().getStreet());
        entity.setCity(buyer.getPrimaryAddress().getCity());
        entity.setState(buyer.getPrimaryAddress().getState());
        entity.setPostalCode(buyer.getPrimaryAddress().getPostalCode());
        entity.setCountry(buyer.getPrimaryAddress().getCountry());
        entity.setAdditionalAddresses(toEmbeddables(buyer.getAdditionalAddresses()));
        entity.setCommercialStatus(buyer.getCommercialStatus());
        return entity;
    }

    private Buyer toDomain(BuyerEntity entity) {
        List<Address> additional = new ArrayList<>();
        if (entity.getAdditionalAddresses() != null) {
            for (AddressEmbeddable address : entity.getAdditionalAddresses()) {
                additional.add(toAddress(address));
            }
        }
        return Buyer.reconstitute(
                entity.getId(),
                entity.getUserId(),
                Address.of(entity.getStreet(), entity.getCity(), entity.getState(),
                        entity.getPostalCode(), entity.getCountry()),
                additional,
                entity.getCommercialStatus());
    }

    private List<AddressEmbeddable> toEmbeddables(List<Address> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return new ArrayList<>();
        }
        List<AddressEmbeddable> embeddables = new ArrayList<>(addresses.size());
        for (Address address : addresses) {
            embeddables.add(toEmbeddable(address));
        }
        return embeddables;
    }

    private AddressEmbeddable toEmbeddable(Address address) {
        AddressEmbeddable embeddable = new AddressEmbeddable();
        embeddable.setStreet(address.getStreet());
        embeddable.setCity(address.getCity());
        embeddable.setState(address.getState());
        embeddable.setPostalCode(address.getPostalCode());
        embeddable.setCountry(address.getCountry());
        return embeddable;
    }

    private Address toAddress(AddressEmbeddable embeddable) {
        return Address.of(embeddable.getStreet(), embeddable.getCity(), embeddable.getState(),
                embeddable.getPostalCode(), embeddable.getCountry());
    }
}
