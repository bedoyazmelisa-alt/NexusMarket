package application.infrastructure.adapter.web.controller;

import application.domain.model.User;
import application.domain.port.in.CreateUserUseCase;
import application.infrastructure.adapter.web.dto.CreateUserRequest;
import application.infrastructure.adapter.web.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes {@link CreateUserUseCase} over HTTP. */
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final CreateUserUseCase createUser;

    public UsersController(CreateUserUseCase createUser) {
        this.createUser = createUser;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest request) {
        User user = createUser.createUser(request.identification(), request.fullName(),
                application.domain.valueobject.Email.of(request.email()), request.role());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }
}
