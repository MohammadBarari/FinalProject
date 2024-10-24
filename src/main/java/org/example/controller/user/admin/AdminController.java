package org.example.controller.user.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.domain.Customer;
import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.dto.ChangeSubHandlerDto;
import org.example.dto.OrderDto;
import org.example.dto.SubHandlerDto;
import org.example.dto.admin.CustomerOutputDtoForReport;
import org.example.dto.admin.EmployeeInputHandlersDto;
import org.example.dto.admin.EmployeeOutputDtoHandlers;
import org.example.dto.admin.EmployeeOutputDtoReport;
import org.example.dto.orders.OrderOutputDto;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.enumirations.TypeOfUser;
import org.example.service.user.admin.AdminService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/saveHandler/{name}")
    public Handler saveHandler(@PathVariable @NotNull  String name){
        return adminService.saveHandler(name);
    }

    @PostMapping("/saveSubHander")
    public SubHandler saveSubHandler(@RequestBody @Valid SubHandlerDto subHandlerDto){
        return adminService.saveSubHandler(subHandlerDto);
    }

    @GetMapping("/saveEmployeeToSubHandler/{employeeId}/{subHandlerId}")
    public void saveEmployeeToSubHandler(@PathVariable @NotNull Integer employeeId,@PathVariable @NotNull Integer subHandlerId){
        adminService.saveEmployeeToSubHandler(employeeId, subHandlerId);
    }
    @GetMapping("/removeEmployeeFromSubHandler/{employeeId}/{subHandlerId}")
    public void removeEmployeeFromSubHandler(@PathVariable @NotNull Integer employeeId
            ,@PathVariable @Digits(integer = 3,fraction = 0) @NotNull Integer subHandlerId){
        adminService.removeEmployeeFromSubHandler(employeeId,subHandlerId);
    }

    @GetMapping("/validateTheEmployee/{employeeId}")
    public void validateTheEmployee(@PathVariable("employeeId") @Digits(integer = 3,fraction = 0) @NotNull
                                        Integer employeeId){
        adminService.acceptEmployee(employeeId);
    }

    @PostMapping("/updatingSubHandler")
    public void detailPriceSubHandlerChanger(@Valid @RequestBody ChangeSubHandlerDto changeSubHandlerDto){
        adminService.detailPriceSubHandlerChanger(changeSubHandlerDto);
    }

    @GetMapping("/findCustomersWithInformations")
    public List<Customer> findCustomersWithInformations(@RequestParam(required = false) String name
                            , @RequestParam(required = false) String lastName,
                                                        @RequestParam(required = false) String email ,
                                                        @RequestParam(required = false) String phone
                                              ){
        return adminService.findCustomerByOptional(name,lastName,email,phone);
    }

    @GetMapping("/findEmployeesWithInformations")
    public List<EmployeeOutputDtoHandlers> findEmployeeWithInformation(
           @RequestBody @Valid EmployeeInputHandlersDto input
    ){
        return adminService.findEmployeesByOptionalInformation(input);
    }
    @GetMapping("/findAllServicesForUser")
    public List<DoneDutiesDto> findAllServicesForUser(@PathVariable Integer id , @PathVariable TypeOfUser typeOfUser){
        return adminService.findPaidWorksById(id,typeOfUser);
    }

    @GetMapping("/findOrdersCustom")
    public List<OrderOutputDto> findOrdersCustom(
            @RequestParam(required = false) LocalDate startDate
            ,
           @RequestParam(required = false) LocalDate endDate
            ,
           @RequestParam(required = false) List<String> subHandlersName
            ,
           @RequestParam(required = false) List<String> handlersName
           ){
        return adminService.optionalFindOrders(startDate,endDate,subHandlersName,handlersName);
    }

    @GetMapping("/employeeReports")
   public List<EmployeeOutputDtoReport> employeeReports(@RequestParam(required = false) LocalDate startDateRegistration,
                                                  @RequestParam(required = false) LocalDate endDateRegistration,
                                                  @RequestParam(required = false) Integer doneWorksStart,
                                                  @RequestParam(required = false) Integer doneWorksEnd,
                                                  @RequestParam(required = false) Integer offerSentStart,
                                                  @RequestParam(required = false) Integer  offerSentEnd){
        return adminService.findEmployeeByReports(startDateRegistration,endDateRegistration,doneWorksStart,doneWorksEnd,offerSentStart,offerSentEnd);
    }
    @GetMapping("/customerReports")
    public List<CustomerOutputDtoForReport> customerReports(@RequestParam(required = false) LocalDate startDateRegistration,
                                                            @RequestParam(required = false) LocalDate endDateRegistration,
                                                            @RequestParam(required = false) Integer paidWorksStart,
                                                            @RequestParam(required = false) Integer paidWorksEnd){
        return adminService.findCustomerByReports(startDateRegistration,endDateRegistration,paidWorksStart,paidWorksEnd);
    }
    @GetMapping("/hi")
    public String hi(){
        return "hi";
    }
}
