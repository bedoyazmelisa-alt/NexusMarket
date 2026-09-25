package application.service;

import application.domain.enums.UserRole;
import application.domain.exception.DuplicateUserException;
import application.domain.model.User;
import application.domain.port.in.CreateUserUseCase;
import application.domain.port.out.UserRepository;
import application.domain.valueobject.Email;

/**
 * Application service for user management. Implements
 * {@link CreateUserUseCase} and enforces the uniqueness rules RG-13
 * (identification) and RG-14 (email) before persisting.
 */
public class UserService implements CreateUserUseCase {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String identification, String fullName, Email email, UserRole role) {
        User user = User.create(identification, fullName, email, role);
        if (userRepository.existsByIdentification(user.getIdentification())) {
            throw new DuplicateUserException(
                    "Identification is already in use: " + user.getIdentification() + " (RG-13)");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateUserException(
                    "Email is already in use: " + user.getEmail().getValue() + " (RG-14)");
        }
        return userRepository.save(user);
    }
}
