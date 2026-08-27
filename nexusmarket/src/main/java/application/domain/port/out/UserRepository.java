package application.domain.port.out;

import application.domain.model.User;
import application.domain.valueobject.Email;

import java.util.Optional;

/**
 * Output port for persisting and retrieving {@link User} aggregates.
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    /**
     * RG-13: user identification must be unique.
     */
    boolean existsByIdentification(String identification);

    /**
     * RG-14: user email must be unique.
     */
    boolean existsByEmail(Email email);
}