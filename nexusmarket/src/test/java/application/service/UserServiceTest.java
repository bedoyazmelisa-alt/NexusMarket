package application.service;

import application.domain.enums.UserRole;
import application.domain.exception.DuplicateUserException;
import application.domain.model.User;
import application.domain.valueobject.Email;
import application.infrastructure.adapter.inmemory.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Domain tests for the data integrity rules RG-13 (identification) and
 * RG-14 (unique user email).
 */
class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(new InMemoryUserRepository());
    }

    @Test
    void shouldRequireUniqueUserEmail() {
        userService.createUser("USR-001", "First User",
                Email.of("first@nexusmarket.com"), UserRole.BUYER);

        // RG-14: a different identification cannot reuse an existing email.
        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> userService.createUser("USR-002", "Second User",
                        Email.of("first@nexusmarket.com"), UserRole.BUYER));
        assertTrue(exception.getMessage().contains("RG-14"));
        assertTrue(exception.getMessage().contains("first@nexusmarket.com"));
    }

    @Test
    void shouldCreateUserWhenEmailIsUnique() {
        User user = userService.createUser("USR-001", "First User",
                Email.of("first@nexusmarket.com"), UserRole.BUYER);

        assertNotNull(user.getId());
        assertEquals("first@nexusmarket.com", user.getEmail().getValue());
        assertEquals(UserRole.BUYER, user.getRole());
    }
}
