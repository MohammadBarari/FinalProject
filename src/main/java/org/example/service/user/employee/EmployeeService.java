package org.example.service.user.employee;

import org.example.domain.Employee;
import org.example.domain.Orders;
import org.example.domain.SubHandler;
import org.example.dto.EmployeeSignUpDto;
import org.example.dto.OfferDto;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.enumirations.TypeOfUser;
import org.example.service.user.BaseUserService;

import java.io.File;
import java.util.List;

public interface EmployeeService  extends BaseUserService<Employee> {
    EmployeeSignUpDto signUpEmployee(EmployeeSignUpDto employee) throws Exception;
    boolean validateEmployee(EmployeeSignUpDto employee, File file) throws Exception;
    OfferDto giveOfferToOrder(OfferDto offerDto);
    void saveEmployee( Employee employee);
    boolean validateImage(File imageFile) throws Exception;
    boolean checkIfImageSizeIsOkay(File imageFile);
    List<SubHandler> findAllSubHandlersForEmployee(Integer employeeId);
    List<Orders> getOrdersForEmployee(Integer employeeId);
    Boolean employeeExistsByEmployeeId(Integer employeeId);
    List<Employee> findEmployeesByOptionalInformation(String name, String lastName, String email, String phone, String handlerName);
    void setUnderReviewState(String email);
    Boolean employeeExistsByEmail(String email);
    void sendToken(String email , TypeOfUser typeOfUser);
    String validateEmployeeEmail(String token);
    List<Orders> findPaidOrders(Integer employeeId);
    List<DoneDutiesDto> findDoneWorksById(Integer id);
}
