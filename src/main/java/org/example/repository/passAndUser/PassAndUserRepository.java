package org.example.repository.passAndUser;

import org.example.domain.PassAndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PassAndUserRepository extends JpaRepository<PassAndUser, Integer> {
    PassAndUser findByUsername(String username);
    PassAndUser findPassAndUserById(int id);

    @Query(value = """
            select * from pass_and_user where username = ?1  and type_of_user = ?2;
""",nativeQuery = true)
    PassAndUser findPass( String userName, String typeOfUser);
}
