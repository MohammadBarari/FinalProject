package org.example.service.user.admin.imp;

import lombok.SneakyThrows;
import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.enumirations.EmployeeState;
import org.example.exeptions.CantRemoveEmployeeFromSubHandler;
import org.example.exeptions.EmployeeIsNotAccepted;
import org.example.exeptions.HandlerIsNull;
import org.example.exeptions.NotFoundSomething;
import org.example.repository.user.admin.AdminRepository;
import org.example.repository.user.admin.imp.AdminRepositoryImp;
import org.example.service.handler.HandlerService;
import org.example.service.handler.imp.HandlerBaseImp;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.subHandler.imp.SubHandlerServiceImp;
import org.example.service.user.admin.AdminService;
import org.example.service.user.employee.EmployeeService;
import org.example.service.user.employee.imp.EmployeeServiceImp;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class AdminServiceImp implements AdminService {
       private final HandlerService handlerService ;
    private final SubHandlerService subHandlerService ;
      private final EmployeeService employeeService ;
      private AdminRepository adminRepository;
    public AdminServiceImp() {
        handlerService = new HandlerBaseImp();
        subHandlerService = new SubHandlerServiceImp();
        employeeService = new EmployeeServiceImp();
        adminRepository = new AdminRepositoryImp();
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
    public void saveEmployeeToSubHandler(Employee employee,Integer subHandlerId) {
        if (employee.getEmployeeState() == EmployeeState.ACCEPTED){
        SubHandler subHandler = subHandlerService.findSubHandlerById(subHandlerId);
        employee.getSubHandlers().add(subHandler);
        employeeService.updateUser(employee);
        }else {
            throw new EmployeeIsNotAccepted();
        }
    }

    @Override
    public void removeEmployeeFromSubHandler(Integer employeeId, Integer subHandlerId) throws NotFoundSomething, CantRemoveEmployeeFromSubHandler {
        try {
            Employee employee = employeeService.findById(employeeId,Employee.class);
            if (Objects.isNull(employee)){
                throw new NotFoundSomething("employee");
            }
            SubHandler  subHandler = subHandlerService.findSubHandlerById(subHandlerId);
            if (Objects.isNull(subHandler)){
                throw new NotFoundSomething("subHandler");
            }
            adminRepository.deleteEmployeeFromSubHandler(employee,subHandlerId);
        }catch (NotFoundSomething e){
            e.printStackTrace();
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new CantRemoveEmployeeFromSubHandler();
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
           Employee employee =  employeeService.findById(employeeId,Employee.class);
           if (Objects.isNull(employee)){
               throw new NotFoundSomething("employee");
           }
        }catch (Exception e){

        }


        if (ifEmployeeIsAccepted(employee)){
            employee.setEmployeeState(EmployeeState.ACCEPTED);

        }
    }

    private boolean ifEmployeeIsAccepted(Employee employee) {
        return true;
    }
}
