package org.example.repository.emailToken;

import org.example.domain.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {

    EmailToken save(EmailToken entity);


    EmailToken findByToken(String token);

}
