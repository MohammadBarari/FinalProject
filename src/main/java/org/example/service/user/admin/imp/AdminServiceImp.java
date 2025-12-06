package org.example.service.user.admin.imp;
import org.example.domain.*;
import org.example.dto.subHandlers.ChangeSubHandlerDto;
import org.example.dto.subHandlers.SubHandlerDto;
import org.example.dto.admin.*;
import org.example.dto.orders.OrderOutputDto;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.enumirations.EmployeeState;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.NotFoundException.NotFoundEmployee;
import org.example.exeptions.NotFoundException.NotFoundHandler;
import org.example.exeptions.NotFoundException.NotFoundSubHandler;
import org.example.exeptions.employee.EmployeeIsNotAccepted;
import org.example.exeptions.duplicate.HandlerIsDuplicate;
import org.example.exeptions.global.FailedDoingOperation;
import org.example.exeptions.password.YouInsertNothing;
import org.example.repository.user.admin.AdminRepository;
import org.example.service.handler.HandlerService;
import org.example.service.mapStruct.EntityMapper;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.user.admin.AdminService;
import org.example.service.user.customer.CustomerService;
import org.example.service.user.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
@Service
public class AdminServiceImp implements AdminService {

    private final HandlerService handlerService ;
    private final SubHandlerService subHandlerService ;
    private final EmployeeService employeeService ;
    private final AdminRepository adminRepository;
    private final CustomerService customerService;
    private final EntityMapper entityMapper;
    @Autowired
    public AdminServiceImp(HandlerService handlerService,
    SubHandlerService subHandlerService ,
    EmployeeService employeeService ,
    AdminRepository adminRepository,
    EntityMapper entityMapper,
    CustomerService customerService
 ) {
        this.handlerService =  handlerService;
        this.subHandlerService =  subHandlerService;
        this.employeeService =  employeeService;
        this.adminRepository =  adminRepository;
        this.customerService = customerService;
        this.entityMapper = entityMapper;
    }
    @Override
    public Handler saveHandler( String handlerName)  {
        Handler handler1 = handlerService.findHandlerByName(handlerName);
        if (handler1 != null) {
            throw new HandlerIsDuplicate();
        }
        Handler handler = Handler.builder().name(handlerName).build();
        handlerService.save(handler);
        return handler;
   }
    @Override
    public SubHandler saveSubHandler(SubHandlerDto subHandlerDto) {
        Handler handler = Optional.ofNullable(handlerService.findHandlerById(subHandlerDto.handlerId())).orElseThrow(() -> new NotFoundHandler("Unable to find Handler with this id : "+ subHandlerDto.handlerId() ));
        SubHandler foundInRep = subHandlerService.findSubHandlerByName(subHandlerDto.name());
        if (foundInRep != null) {
            throw new HandlerIsDuplicate();
        }
        SubHandler subHandler = entityMapper.dtoToSubHandler(subHandlerDto);
        subHandler.setHandler(handler);
        subHandlerService.saveSubHandler(subHandler);
        return subHandler;
    }

    @Override
    public void saveEmployeeToSubHandler(Integer employeeId,Integer subHandlerId) {
       Employee employee = Optional.ofNullable(employeeService.findById(employeeId,Employee.class)).orElseThrow(()-> new NotFoundEmployee("Unable to find Employee with this id : "+ employeeId ));
       if (employee.getEmployeeState() == EmployeeState.ACCEPTED){
          SubHandler subHandler = Optional.ofNullable(subHandlerService.findSubHandlerById(subHandlerId)).orElseThrow(()-> new NotFoundHandler("Unable to find SubHandler with this id : "+ subHandlerId ));
          if (Objects.isNull(employee.getSubHandlers()))
          {
              Set<SubHandler> handlers = Set.of(subHandler);
              employee.setSubHandlers(handlers);
          }else {
            employee.getSubHandlers().add(subHandler);
          }
          employeeService.updateUser(employee);
        }else {
           throw new EmployeeIsNotAccepted();
        }
    }
    @Override
    public void detailPriceSubHandlerChanger(ChangeSubHandlerDto changeSubHandlerDto)  {
            SubHandler subHandler;

            subHandler = subHandlerService.findSubHandlerById(changeSubHandlerDto.id());
            if (subHandler == null) {
                throw new NotFoundSubHandler();
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
    public List<CustomerOutput> findCustomerByOptional(FindFilteredCustomerDto input) {
        List<Customer> customers = customerService.findCustomerByOptional(input);
        List<CustomerOutput> customerOutputs = new ArrayList<>();
        for (Customer customer : customers) {
            customerOutputs.add(new CustomerOutput(customer.getId(),customer.getName(),customer.getLast_name(),customer.getEmail(),customer.getPhone(),customer.getTimeOfRegistration(),customer.isActive()));
        }
        return customerOutputs;
    }

    @Override
    public List<EmployeeOutputDtoHandlers> findEmployeesByOptionalInformation(EmployeeInputHandlersDto input) {
        return employeeService.findEmployeesByOptionalInformation(input);
    }

    public List<DoneDutiesDto> findPaidWorksById(Integer id, TypeOfUser typeOfUser) {
        switch (typeOfUser) {
            case EMPLOYEE -> {
                return employeeService.findDoneWorksById(id);}
            case CUSTOMER -> {
                return customerService.findDoneWorksById(id);}
        }
        throw new FailedDoingOperation("Unable to find DoneWorks by id : "+ id);
    }

    @Override
    @Transactional
    public List<OrderOutputDto> optionalFindOrders(FindFilteredOrdersDto input) {
        List<OrderOutputDto> ordersOutputDtos = new ArrayList<>();
        employeeService.optionalFindOrders(input).forEach(orders -> ordersOutputDtos.add(new OrderOutputDto(orders.getOfferedPrice(),orders.getDetail()
                ,orders.getSubHandler().getName(),orders.getTimeOfWork()
                ,orders.getAddress(),orders.getOrderState()
                ,orders.getCustomer().getName()+ " " + orders.getCustomer().getLast_name(),orders.getCustomer().getId(),orders.getEmployee() != null?orders.getEmployee().getName():null,orders.getEmployee()!=null?orders.getEmployee().getId():null,orders.getScore(),orders.getComment())));
        return ordersOutputDtos;
    }

    @Override
    public List<EmployeeOutputDtoReport> findEmployeeByReports(FindFilteredEmployeeDto input) {
        return employeeService.findEmployeeByReports(input);
    }

    @Override
    @Transactional
    public void removeEmployeeFromSubHandler(Integer employeeId, Integer subHandlerId)  {
            Employee employee = Optional.ofNullable(employeeService.findById(employeeId,Employee.class)).orElseThrow(NotFoundEmployee::new);
            SubHandler  subHandler = Optional.ofNullable(subHandlerService.findSubHandlerById(subHandlerId)).orElseThrow(()->new NotFoundSubHandler("Unable to find SubHandler with this id : "+ subHandlerId ));
            adminRepository.deleteEmployeeFromSubHandler(employeeId,subHandlerId);
    }

    @Override
    public void acceptEmployee(Integer employeeId){
           Employee employee = employeeService.findById(employeeId,Employee.class);
            if (ifEmployeeIsAccepted(employee)){
                employee.setEmployeeState(EmployeeState.ACCEPTED);
                employeeService.updateUser(employee);
            }
    }

    private boolean ifEmployeeIsAccepted(Employee employee) {
        return true;
    }
    @Override
    public List<CustomerOutputDtoForReport> findCustomerByReports(FindCustomerByFilterDto input) {
        return customerService.findCustomerByReports(input);
    }

}
