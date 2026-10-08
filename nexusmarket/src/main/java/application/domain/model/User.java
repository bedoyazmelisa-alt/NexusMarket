package application.domain.model;

import application.domain.enums.UserRole;
import application.domain.enums.UserStatus;
import application.domain.exception.InvalidUserStatusException;
import application.domain.valueobject.Email;
import lombok.Getter;

/**
 * A person authorized to interact with NexusMarket. Has a unique identity,
 * unique identification, unique email and exactly one role (RG-02, RG-13, RG-14).
 */
@Getter
public class User {

    private Long id;
    private String identification;
    private String fullName;
    private Email email;
    private UserRole role;
    private UserStatus status;

    private User() {
    }

    public static User create(String identification, String fullName, Email email, UserRole role) {
        if (identification == null || identification.isBlank()) {
            throw new IllegalArgumentException("Identification must not be blank");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name must not be blank");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email must not be null");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role must not be null");
        }
        User user = new User();
        user.identification = identification.trim();
        user.fullName = fullName.trim();
        user.email = email;
        user.role = role;
        user.status = UserStatus.ACTIVE;
        return user;
    }

    /**
     * Rebuilds a user from persisted data. Unlike {@link #create}, it preserves
     * the stored state exactly (status, id) and skips creation-time defaults;
     * used only by persistence adapters to rehydrate the domain.
     */
    public static User reconstitute(Long id, String identification, String fullName,
                                    Email email, UserRole role, UserStatus status) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (identification == null || identification.isBlank()) {
            throw new IllegalArgumentException("Identification must not be blank");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name must not be blank");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email must not be null");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role must not be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
        User user = new User();
        user.id = id;
        user.identification = identification.trim();
        user.fullName = fullName.trim();
        user.email = email;
        user.role = role;
        user.status = status;
        return user;
    }

    public void activate() {
        if (status == UserStatus.ACTIVE) {
            throw new InvalidUserStatusException("User is already active");
        }
        this.status = UserStatus.ACTIVE;
    }

    public void block() {
        if (status == UserStatus.BLOCKED) {
            throw new InvalidUserStatusException("User is already blocked");
        }
        this.status = UserStatus.BLOCKED;
    }

    public void changeRole(UserRole newRole) {
        if (newRole == null) {
            throw new IllegalArgumentException("Role must not be null");
        }
        this.role = newRole;
    }

    public void changeStatus(UserStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
        this.status = newStatus;
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id already assigned");
        }
        this.id = id;
    }
}