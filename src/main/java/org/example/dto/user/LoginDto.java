package org.example.dto.user;

import jakarta.validation.constraints.NotNull;

public record LoginDto( @NotNull String username,
                        @NotNull String password) {
}
