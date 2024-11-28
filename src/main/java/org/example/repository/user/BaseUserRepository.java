package org.example.repository.user;

import org.example.domain.Users;

public interface BaseUserRepository<T extends Users>{

    void save(T user) ;

    void update(T user);

    Object find(String userName, Class<T> userType);

    T findById(int id,Class<T> tClass );

}
