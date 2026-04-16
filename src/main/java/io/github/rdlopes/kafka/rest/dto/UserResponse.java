package io.github.rdlopes.kafka.rest.dto;

import java.util.List;

public record UserResponse(
        String id,
        String name,
        String email,
        long timestamp,
        List<AccountResponse> accounts
) {
}
