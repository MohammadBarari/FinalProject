package org.example.service.user.admin;

import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.dto.ChangeSubHandlerDto;
import org.example.dto.SaveSubHandlerDto;
import org.example.enumirations.EmployeeState;
import org.example.exeptions.*;

public interface AdminService {
    Handler saveHandler(String handlerName) ;
    SubHandler saveSubHandler(SaveSubHandlerDto saveSubHandlerDto);
    void saveEmployeeToSubHandler(Integer employeeId,Integer subHandlerId);
    void removeEmployeeFromSubHandler(Integer employeeId, Integer subHandlerId) ;
    void validateTheEmployee(Integer employeeId);
    void detailPriceSubHandlerChanger(ChangeSubHandlerDto changeSubHandlerDto);

}
