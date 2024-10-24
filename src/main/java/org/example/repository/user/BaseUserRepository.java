package org.example.repository.user;

import org.example.domain.PassAndUser;
import org.example.domain.Users;

public interface BaseUserRepository<T extends Users>{
    void save(T user) ;
    void update(T user);
    void saveUserAndPass(PassAndUser passAndUser);
    PassAndUser findPass(PassAndUser passAndUser);
    void updatePass(PassAndUser passAndUser);
    Object find(String userName, Class<T> userType);
    T findById(int id,Class<T> tClass );

}
