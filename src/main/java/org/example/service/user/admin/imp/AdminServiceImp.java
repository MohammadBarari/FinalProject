package org.example.service.user.admin.imp;

import lombok.SneakyThrows;
import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.enumirations.EmployeeState;
import org.example.exeptions.*;
import org.example.repository.user.admin.AdminRepository;
import org.example.service.exceptionHandling.NullExceptionHandling;
import org.example.service.handler.HandlerService;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.user.admin.AdminService;
import org.example.service.user.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service

public class AdminServiceImp implements AdminService {

    private final HandlerService handlerService ;
    private final SubHandlerService subHandlerService ;
    private final EmployeeService employeeService ;
    private final AdminRepository adminRepository;
   @Autowired
    public AdminServiceImp(HandlerService handlerService,
    SubHandlerService subHandlerService ,
    EmployeeService employeeService ,
    AdminRepository adminRepository
 ) {
        this.handlerService =  handlerService;
        this.subHandlerService =  subHandlerService;
        this.employeeService =  employeeService;
        this.adminRepository =  adminRepository;
    }

    @Override
    public void saveHandler(Handler handler) {
        handlerService.save(handler);
    }

    @SneakyThrows
    @Override
    public void saveSubHandler(SubHandler subHandler,Integer handlerId) {
        Handler handler = handlerService.findHandlerById(handlerId);
        if (Objects.isNull(handler)){
            throw new HandlerIsNull();
    }
        subHandler.setHandler(handler);
        subHandlerService.saveSubHandler(subHandler);
    }
    @SneakyThrows
    @Override
    public void saveEmployeeToSubHandler(Integer employeeId,Integer subHandlerId) {
       Employee employee = employeeService.findById(employeeId,Employee.class);
       //1
       if (Objects.isNull(employee)){
           throw new NotFoundEmployee();
       }
       //2
       if (employee.getEmployeeState() == EmployeeState.ACCEPTED){
          SubHandler subHandler = subHandlerService.findSubHandlerById(subHandlerId);
         //3
          if (Objects.isNull(subHandler)){
              throw new HandlerIsNull();
          }
          if (Objects.isNull(employee.getSubHandlers()))
          {
              Set<SubHandler> handlers = Set.of(subHandler);
              employee.setSubHandlers(handlers);
          }else {
            employee.getSubHandlers().add(subHandler);
          }
          employeeService.updateUser(employee);
        }else {
           //4
           throw new EmployeeIsNotAccepted();
        }
    }

    @Override
    public void removeEmployeeFromSubHandler(Integer employeeId, Integer subHandlerId) throws NotFoundSomething, CantRemoveEmployeeFromSubHandler, NotFoundEmployee, SubHandlerNull {
        try {
            Employee employee = employeeService.findById(employeeId,Employee.class);
            if (Objects.isNull(employee)){
                throw new NotFoundEmployee();
            }
            SubHandler  subHandler = subHandlerService.findSubHandlerById(subHandlerId);
            if (Objects.isNull(subHandler)){
                throw new SubHandlerNull();
            }
            adminRepository.deleteEmployeeFromSubHandler(employee,subHandlerId);

        }catch (Exception e){
            e.printStackTrace();
            if (!(e instanceof NotFoundEmployee ||
                   e instanceof  SubHandlerNull)){

                throw new CantRemoveEmployeeFromSubHandler();
            }else {
                throw e;
            }
        }

    }


    @Override
    public void changeEmployeeState(Integer employeeId, EmployeeState employeeState) {
        Employee employee = employeeService.findById(employeeId,Employee.class);
        employee.setEmployeeState(employeeState);
        employeeService.updateUser(employee);
    }

    void validateTheEmployee(Integer employeeId) throws NotFoundSomething, CantRemoveEmployeeFromSubHandler {
        try {
           Employee employee = (Employee) NullExceptionHandling.getInstance().handlingNullExceptions
                   (employeeService.findById(employeeId,Employee.class),Employee.class);
            if (ifEmployeeIsAccepted(employee)){
                employee.setEmployeeState(EmployeeState.ACCEPTED);
            }
        }catch (Exception e){
            throw e;
        }
    }

    private boolean ifEmployeeIsAccepted(Employee employee) {
        return true;
    }
}
