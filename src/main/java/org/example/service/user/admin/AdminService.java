package org.example.service.user.admin;

import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.enumirations.EmployeeState;

public interface AdminService {
    void saveHandler(Handler handler);
    void saveSubHandler(SubHandler subHandler,Integer handlerId);
    void saveEmployeeToSubHandler(Employee employee,Integer subHandlerId);
    void deleteEmployeeFromSubHandler(Employee employee,Integer subHandlerId);
    void changeEmployeeState(Integer employee , EmployeeState employeeState);
}
