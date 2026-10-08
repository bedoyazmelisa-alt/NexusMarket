package application.infrastructure.adapter.jpa;

import application.infrastructure.adapter.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link UserEntity}. Technical query layer only;
 * domain logic never sees this interface.
 */
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByIdentification(String identification);

    boolean existsByEmail(String email);
}
