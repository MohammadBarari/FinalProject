package org.example.repository.passAndUser;

import org.example.domain.PassAndUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassAndUserRepository extends JpaRepository<PassAndUser, Integer> {
    PassAndUser findByUsername(String username);
}
