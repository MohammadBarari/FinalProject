package org.example.service.mainService.imp;//package org.example.service.mainService.imp;
//import org.example.domain.Customer;
//import org.example.dto.servisesDone.DoneDutiesDto;
//import org.example.service.user.customer.CustomerService;
//import org.example.service.user.employee.EmployeeService;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CombinedUserClassFromEmployee {
//    private final CustomerService customerService;
//    private final EmployeeService employeeService;
//    public CombinedUserClassFromEmployee(CustomerService customerService,EmployeeService employeeService) {
//        this.customerService = customerService;
//        this.employeeService = employeeService;
//    }
//    public List<DoneDutiesDto> getCustomer(Integer id, Integer customerId) {
//        Customer customer =  customerService.findById(id,Customer.class);
//        return employeeService.findDoneWorksById(id,customer);
//    }
//}
