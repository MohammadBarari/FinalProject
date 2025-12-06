package org.example.service.user.employee;

import org.example.domain.Employee;
import org.example.dto.employee.EmployeeSignUpDto;
import org.example.dto.admin.EmployeeInputHandlersDto;
import org.example.dto.admin.EmployeeOutputDtoHandlers;
import org.example.dto.admin.EmployeeOutputDtoReport;
import org.example.dto.admin.FindFilteredEmployeeDto;
import org.example.dto.employee.EmployeeLoginDtoOutput;
import org.example.dto.employee.OfferDto;
import org.example.dto.employee.OrderOutputEmployee;
import org.example.dto.employee.SubHandlerOutput;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.dto.user.OrdersOutputDtoUser;
import org.example.enumirations.TypeOfUser;
import org.example.service.user.BaseUserService;

import java.util.List;

public interface EmployeeService  extends BaseUserService<Employee> {
    EmployeeSignUpDto signUpEmployee(EmployeeSignUpDto employee) throws Exception;
    boolean validateEmployee(EmployeeSignUpDto employee, String file) throws Exception;
    OfferDto giveOfferToOrder(OfferDto offerDto);
    void saveEmployee( Employee employee);
    List<SubHandlerOutput> findAllSubHandlersForEmployee(Integer employeeId);
    Boolean employeeExistsByEmployeeId(Integer employeeId);
    List<EmployeeOutputDtoHandlers> findEmployeesByOptionalInformation(EmployeeInputHandlersDto input);
    void setUnderReviewState(String email);
    Boolean employeeExistsByEmail(String email);
    void sendToken(String email , TypeOfUser typeOfUser);
    String validateEmployeeEmail(String token);
    List<DoneDutiesDto> findDoneWorksById(Integer id);
    List<EmployeeOutputDtoReport> findEmployeeByReports(FindFilteredEmployeeDto input);
    List<OrdersOutputDtoUser> optionalSelectOrdersForEmployee(Integer employeeId, String orderState);
    Double getCreditAmount(Integer id);
    EmployeeLoginDtoOutput login(String user, String pass) ;
    List<OrderOutputEmployee> findOrdersForEmployee(Integer employeeId);
}
