package org.example.service.emailToken;

import org.example.domain.EmailToken;
import org.example.enumirations.TypeOfUser;

public interface EmailTokenService {
    void validateToken(String Token);
    void remove(EmailToken emailToken);
    EmailToken findByToken(String token);
}
