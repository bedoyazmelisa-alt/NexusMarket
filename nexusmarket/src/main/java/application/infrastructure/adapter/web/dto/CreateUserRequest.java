package application.infrastructure.adapter.web.dto;

import application.domain.enums.UserRole;

/** Request to create a user. */
public record CreateUserRequest(String identification, String fullName, String email, UserRole role) {
}
