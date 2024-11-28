package org.example.service.user;

import org.example.domain.Orders;
import org.example.domain.PassAndUser;
import org.example.domain.Users;
import org.example.dto.password.ChangingPasswordDto;
import org.example.dto.admin.FindFilteredOrdersDto;

import java.util.List;


public interface BaseUserService<T extends Users> {
    void signUp(T t);

    void updateUser(T t);

    boolean validatePassWord(String pass);

    void savePassAndUser(PassAndUser passAndUser);

    boolean checkIfNotDuplicateUser(String user);

    String changingPassword(ChangingPasswordDto changingPasswordDto);

    T findById(int id,Class<T> tClass);

    List<Orders> optionalFindOrders(FindFilteredOrdersDto input);

}

