package org.example.service.user.admin;

import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.domain.SubHandler;

public interface AdminService {
    void saveHandler(Handler handler);
    void saveSubHandler(SubHandler subHandler,Integer handlerId);
    void saveEmployeeToSubHandler(Employee employee,Integer subHandlerId);
    void deleteEmployeeFromSubHandler(Employee employee,Integer subHandlerId);
}
