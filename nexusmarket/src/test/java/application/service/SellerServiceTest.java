package application.service;

import application.domain.enums.UserRole;
import application.domain.exception.SellerRegistrationNotAllowedException;
import application.domain.model.Seller;
import application.domain.model.User;
import application.domain.valueobject.Email;
import application.infrastructure.adapter.inmemory.InMemorySellerRepository;
import application.infrastructure.adapter.inmemory.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Domain tests for the seller incorporation rules (RG-01, RG-04, RG-05):
 * sellers cannot self-register and only an Administrator can register one.
 */
class SellerServiceTest {

    private SellerService sellerService;
    private InMemorySellerRepository sellerRepository;
    private Long targetUserId;

    @BeforeEach
    void setUp() {
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        sellerRepository = new InMemorySellerRepository();
        sellerService = new SellerService(sellerRepository, userRepository);

        User target = userRepository.save(User.create(
                "USR-001", "Target User", Email.of("target@nexusmarket.com"), UserRole.BUYER));
        targetUserId = target.getId();
    }

    @Test
    void shouldNotAllowSellerSelfRegistration() {
        User selfSeller = User.create(
                "SLR-001", "Would-be Seller", Email.of("seller@nexusmarket.com"), UserRole.SELLER);

        // RG-04: a seller cannot register itself.
        SellerRegistrationNotAllowedException selfRegistration = assertThrows(
                SellerRegistrationNotAllowedException.class,
                () -> sellerService.registerSeller(selfSeller, targetUserId, "My own business"));
        assertTrue(selfRegistration.getMessage().contains("RG-05"));

        // RG-01: without an authenticated actor nobody can register a seller.
        SellerRegistrationNotAllowedException anonymous = assertThrows(
                SellerRegistrationNotAllowedException.class,
                () -> sellerService.registerSeller(null, targetUserId, "My own business"));
        assertTrue(anonymous.getMessage().contains("RG-01"));

        assertTrue(sellerRepository.findByUserId(targetUserId).isEmpty());
    }

    @Test
    void shouldAllowAdministratorToRegisterSeller() {
        User administrator = User.create(
                "ADM-001", "Administrator", Email.of("admin@nexusmarket.com"), UserRole.ADMINISTRATOR);

        Seller seller = sellerService.registerSeller(administrator, targetUserId, "ACME Inc.");

        assertNotNull(seller.getId());
        assertEquals(targetUserId, seller.getUserId());
        assertEquals("ACME Inc.", seller.getBusinessInformation());
        assertTrue(sellerRepository.findByUserId(targetUserId).isPresent());
    }
}
