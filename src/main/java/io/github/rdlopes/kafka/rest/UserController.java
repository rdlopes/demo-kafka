package io.github.rdlopes.kafka.rest;

import io.github.rdlopes.kafka.persistence.UserEntity;
import io.github.rdlopes.kafka.rest.dto.AccountResponse;
import io.github.rdlopes.kafka.rest.dto.UserResponse;
import io.github.rdlopes.kafka.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public List<UserResponse> getAllUsers() {
    return userService.findAllUsers()
                      .stream()
                      .map(this::toUserResponse)
                      .toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
    return userService.findUserById(id)
                      .map(this::toUserResponse)
                      .map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound()
                                            .build());
  }

  private UserResponse toUserResponse(UserEntity entity) {
    return new UserResponse(
      entity.getId(),
      entity.getName(),
      entity.getEmail(),
      entity.getTimestamp(),
      entity.getAccounts()
            .stream()
            .map(account -> new AccountResponse(account.getId(), account.getTimestamp()))
            .toList()
    );
  }
}
