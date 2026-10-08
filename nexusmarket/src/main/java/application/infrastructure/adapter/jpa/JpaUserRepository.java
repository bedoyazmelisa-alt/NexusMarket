package application.infrastructure.adapter.jpa;

import application.domain.model.User;
import application.domain.port.out.UserRepository;
import application.domain.valueobject.Email;
import application.infrastructure.adapter.jpa.entity.UserEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MySQL/JPA implementation of {@link UserRepository}. Translates between the
 * persistence model ({@link UserEntity}) and the domain model; no business
 * rules live here.
 */
@Repository
@Profile("!in-memory")
public class JpaUserRepository implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public JpaUserRepository(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = jpaRepository.save(entity);
        if (user.getId() == null) {
            user.assignId(saved.getId());
        }
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsByIdentification(String identification) {
        if (identification == null) {
            return false;
        }
        return jpaRepository.existsByIdentification(identification);
    }

    @Override
    public boolean existsByEmail(Email email) {
        if (email == null) {
            return false;
        }
        return jpaRepository.existsByEmail(email.getValue());
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setIdentification(user.getIdentification());
        entity.setFullName(user.getFullName());
        entity.setEmail(user.getEmail().getValue());
        entity.setRole(user.getRole());
        entity.setStatus(user.getStatus());
        return entity;
    }

    private User toDomain(UserEntity entity) {
        return User.reconstitute(
                entity.getId(),
                entity.getIdentification(),
                entity.getFullName(),
                Email.of(entity.getEmail()),
                entity.getRole(),
                entity.getStatus());
    }
}
