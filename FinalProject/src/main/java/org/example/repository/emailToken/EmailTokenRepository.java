package org.example.repository.emailToken;

import org.example.domain.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {

    Optional<EmailToken> findByEmail(String email);
    boolean existsByEmail(String email);
    EmailToken findByToken(String token);

}
