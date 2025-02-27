package org.example.service.emailToken;

import org.example.domain.EmailToken;
import org.example.enumirations.TypeOfUser;

public interface EmailTokenService {
    void sendEmail(String email, TypeOfUser typeOfUser);
    void validateToken(String Token);
    void remove(EmailToken emailToken);
    EmailToken findByToken(String token);
}
