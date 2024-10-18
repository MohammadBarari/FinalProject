package org.example.service.user.admin.imp;

import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.example.domain.Customer;
import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.dto.ChangeSubHandlerDto;
import org.example.dto.SaveSubHandlerDto;
import org.example.enumirations.EmployeeState;
import org.example.exeptions.*;
import org.example.repository.user.admin.AdminRepository;
import org.example.service.exceptionHandling.NullExceptionHandling;
import org.example.service.handler.HandlerService;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.user.admin.AdminService;
import org.example.service.user.customer.CustomerService;
import org.example.service.user.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service

public class AdminServiceImp implements AdminService {

    private final HandlerService handlerService ;
    private final SubHandlerService subHandlerService ;
    private final EmployeeService employeeService ;
    private final AdminRepository adminRepository;
    private final CustomerService customerService;
    @Autowired
    public AdminServiceImp(HandlerService handlerService,
    SubHandlerService subHandlerService ,
    EmployeeService employeeService ,
    AdminRepository adminRepository,
                           CustomerService customerService
 ) {
        this.handlerService =  handlerService;
        this.subHandlerService =  subHandlerService;
        this.employeeService =  employeeService;
        this.adminRepository =  adminRepository;
        this.customerService = customerService;
    }
    @SneakyThrows
    @Override
    public Handler saveHandler( String handlerName)  {
            Handler handler1 = handlerService.findHandlerByName(handlerName);
            if (handler1 != null) {
                throw new HandlerIsDuplicate();
            }
        Handler handler = new Handler();
        handler.setName(handlerName);
        handlerService.save(handler);
        return handler;
   }

    @SneakyThrows
    @Override
    public SubHandler saveSubHandler( SaveSubHandlerDto saveSubHandlerDto) {
        Handler handler = handlerService.findHandlerById(saveSubHandlerDto.handlerId());
        if (Objects.isNull(handler)){
            throw new HandlerIsNull();
    }
        SubHandler subHandler = new SubHandler();
        subHandler.setName(saveSubHandlerDto.name());
        subHandler.setBasePrice(saveSubHandlerDto.basePrice());
        subHandler.setDetail(saveSubHandlerDto.detail());
        subHandler.setHandler(handler);
        subHandlerService.saveSubHandler(subHandler);
        return subHandler;
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
    @SneakyThrows
    public void detailPriceSubHandlerChanger(ChangeSubHandlerDto changeSubHandlerDto)  {
            SubHandler subHandler = null;

            subHandler = subHandlerService.findSubHandlerById(changeSubHandlerDto.id());
            if (subHandler == null) {
                throw new SubHandlerNull();
            }
            if (!Objects.isNull(changeSubHandlerDto.detail())) {
                subHandler.setDetail(changeSubHandlerDto.detail());
            }
            if (!Objects.isNull(changeSubHandlerDto.basePrice())) {
                subHandler.setBasePrice(changeSubHandlerDto.basePrice());
            }
            if (Objects.isNull(changeSubHandlerDto.basePrice())
                    &&
                    Objects.isNull(changeSubHandlerDto.detail())) {
                throw new YouInsertNothing();
            }
            subHandlerService.updateSubHandler(subHandler);
    }

    @Override
    public List<Customer> findCustomerByOptional(String name, String lastName, String email, String phone) {
        return customerService.findCustomerByOptional(name, lastName, email, phone);
    }

    @Override
    public List<Employee> findEmployeesByOptionalInformation(String name, String lastName, String email, String phone, String handlerName) {
        return employeeService.findEmployeesByOptionalInformation(name, lastName, email, phone, handlerName);
    }

    @Override
    @SneakyThrows
    @Transactional
    public void removeEmployeeFromSubHandler(Integer employeeId, Integer subHandlerId)  {
            Employee employee = employeeService.findById(employeeId,Employee.class);
            if (Objects.isNull(employee)){
                throw new NotFoundEmployee();
            }
            SubHandler  subHandler = subHandlerService.findSubHandlerById(subHandlerId);
            if (Objects.isNull(subHandler)){
                throw new SubHandlerNull();
            }
            adminRepository.deleteEmployeeFromSubHandler(employee,subHandlerId);
    }


//    @Override
//    public void changeEmployeeState(Integer employeeId, EmployeeState employeeState) {
//        Employee employee = employeeService.findById(employeeId,Employee.class);
//        employee.setEmployeeState(employeeState);
//        employeeService.updateUser(employee);
//    }


    @SneakyThrows
    @Override
    public void validateTheEmployee(Integer employeeId){
           Employee employee = (Employee) NullExceptionHandling.getInstance().handlingNullExceptions
                   (employeeService.findById(employeeId,Employee.class),Employee.class);
            if (ifEmployeeIsAccepted(employee)){
                employee.setEmployeeState(EmployeeState.ACCEPTED);
                employeeService.updateUser(employee);
            }
    }

    private boolean ifEmployeeIsAccepted(Employee employee) {
        return true;
    }


}
