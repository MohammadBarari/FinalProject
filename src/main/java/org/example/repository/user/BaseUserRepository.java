package org.example.repository.user;

import org.example.domain.PassAndUser;
import org.example.domain.User;

public interface BaseUserRepository<T extends User> {
    void save(T user) ;
    void update(T user);
    void saveUserAndPass(PassAndUser passAndUser);
    PassAndUser findPass(PassAndUser passAndUser);
    void updatePass(PassAndUser passAndUser);
    T find(String userName, Class<T> userType);
    T findById(int id,Class<T> tClass );
}
