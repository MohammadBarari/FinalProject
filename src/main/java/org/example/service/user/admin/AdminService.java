package org.example.service.user.admin;

import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.enumirations.EmployeeState;
import org.example.exeptions.CantRemoveEmployeeFromSubHandler;
import org.example.exeptions.NotFoundEmployee;
import org.example.exeptions.NotFoundSomething;
import org.example.exeptions.SubHandlerNull;

public interface AdminService {
    void saveHandler(Handler handler);
    void saveSubHandler(SubHandler subHandler,Integer handlerId);
    void saveEmployeeToSubHandler(Integer employeeId,Integer subHandlerId);
    void removeEmployeeFromSubHandler(Integer employeeId, Integer subHandlerId) throws NotFoundSomething, CantRemoveEmployeeFromSubHandler, NotFoundEmployee, SubHandlerNull;
    void changeEmployeeState(Integer employeeId, EmployeeState employeeState);
}
