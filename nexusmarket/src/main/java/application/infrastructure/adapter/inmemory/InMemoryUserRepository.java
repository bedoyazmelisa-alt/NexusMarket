package application.infrastructure.adapter.inmemory;

import application.domain.model.User;
import application.domain.port.out.UserRepository;
import application.domain.valueobject.Email;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * In-memory {@link UserRepository}. Stand-in until the MySQL adapter exists.
 */
@Repository
public class InMemoryUserRepository extends InMemoryRepository<User> implements UserRepository {

    public InMemoryUserRepository() {
        super(User::getId, User::assignId);
    }

    @Override
    public User save(User user) {
        return persist(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return row(id);
    }

    @Override
    public boolean existsByIdentification(String identification) {
        if (identification == null) {
            return false;
        }
        return all().stream().anyMatch(user -> identification.equals(user.getIdentification()));
    }

    @Override
    public boolean existsByEmail(Email email) {
        if (email == null) {
            return false;
        }
        return all().stream().anyMatch(user -> email.equals(user.getEmail()));
    }
}
