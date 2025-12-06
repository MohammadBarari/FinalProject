package org.example.service.emailToken;

import org.example.domain.EmailToken;

public interface EmailTokenService {
    void validateToken(String Token);

    EmailToken findByToken(String token);
}
