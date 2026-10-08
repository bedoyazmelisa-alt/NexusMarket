package application.infrastructure.adapter.web.dto;

import application.domain.enums.UserRole;
import application.domain.enums.UserStatus;
import application.domain.model.User;

/** User view returned by the API (primitives only — no domain types). */
public record UserResponse(Long id, String identification, String fullName, String email,
                           UserRole role, UserStatus status) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getIdentification(), user.getFullName(),
                user.getEmail().getValue(), user.getRole(), user.getStatus());
    }
}
