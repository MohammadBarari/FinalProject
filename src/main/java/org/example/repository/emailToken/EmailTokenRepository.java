package org.example.repository.emailToken;

import org.example.domain.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {

    boolean existsByEmail(String email);
    EmailToken findByToken(String token);

}
