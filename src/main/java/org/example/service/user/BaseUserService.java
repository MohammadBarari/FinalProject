package org.example.service.user;

import org.example.domain.PassAndUser;
import org.example.domain.Users;
import org.example.domain.Users;
import org.example.dto.ChangingPasswordDto;

public interface BaseUserService<T extends Users> {
    void signUp(T t,PassAndUser passAndUser);

    void updateUser(T t);

    boolean validatePassWord(String pass);

    void savePassAndUser(PassAndUser passAndUser);

    T login(String user, String pass);

    boolean checkIfNotDuplicateUser(String user);

    void changingPassword(ChangingPasswordDto changingPasswordDto);

    T findById(int id,Class<T> tClass);
}

