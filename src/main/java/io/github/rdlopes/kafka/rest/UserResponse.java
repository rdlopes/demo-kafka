package io.github.rdlopes.kafka.rest;

import io.github.rdlopes.kafka.persistence.AccountEntity;
import io.github.rdlopes.kafka.persistence.UserEntity;

import java.util.List;

public record UserResponse(
  String id,
  String name,
  String email,
  long timestamp,
  List<AccountResponse> accounts
) {

  public record AccountResponse(
    String id,
    long timestamp
  ) {

    public static AccountResponse from(AccountEntity accountEntity) {
      return new AccountResponse(accountEntity.getId(), accountEntity.getTimestamp());
    }
  }

  public static UserResponse from(UserEntity user) {
    return new UserResponse(
      user.getId(),
      user.getName(),
      user.getEmail(),
      user.getTimestamp(),
      user.getAccounts()
          .stream()
          .map(AccountResponse::from)
          .toList()
    );
  }
}
