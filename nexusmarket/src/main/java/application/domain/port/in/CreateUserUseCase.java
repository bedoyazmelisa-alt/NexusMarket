package application.domain.port.in;

import application.domain.enums.UserRole;
import application.domain.model.User;
import application.domain.valueobject.Email;

/**
 * Input port: creates a user with a single role (RG-02).
 */
public interface CreateUserUseCase {

    User createUser(String identification, String fullName, Email email, UserRole role);
}