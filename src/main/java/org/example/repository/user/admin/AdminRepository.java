package org.example.repository.user.admin;

import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.domain.SubHandler;

public interface AdminRepository {
    void saveHandler(Handler handler);
    void saveSubHandler(SubHandler subHandler, Integer handlerId);
    void saveEmployeeToSubHandler(Employee employee, Integer subHandlerId);
    void deleteEmployeeFromSubHandler(Employee employee,Integer subHandlerId);
}
