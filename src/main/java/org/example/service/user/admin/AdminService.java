package org.example.service.user.admin;

import org.example.domain.Customer;
import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.dto.ChangeSubHandlerDto;
import org.example.dto.SubHandlerDto;
import org.example.dto.admin.CustomerOutputDtoForReport;
import org.example.dto.admin.EmployeeInputHandlersDto;
import org.example.dto.admin.EmployeeOutputDtoHandlers;
import org.example.dto.admin.EmployeeOutputDtoReport;
import org.example.dto.orders.OrderOutputDto;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.enumirations.TypeOfUser;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {
    Handler saveHandler(String handlerName) ;
    SubHandler saveSubHandler(SubHandlerDto subHandlerDto);
    void saveEmployeeToSubHandler(Integer employeeId,Integer subHandlerId);
    void removeEmployeeFromSubHandler(Integer employeeId, Integer subHandlerId) ;
    void acceptEmployee(Integer employeeId);
    void detailPriceSubHandlerChanger(ChangeSubHandlerDto changeSubHandlerDto);
    List<Customer> findCustomerByOptional(String name, String lastName, String email, String phone);
    List<EmployeeOutputDtoHandlers> findEmployeesByOptionalInformation(EmployeeInputHandlersDto input);
    List<DoneDutiesDto> findPaidWorksById(Integer id, TypeOfUser typeOfUser);
    List<OrderOutputDto> optionalFindOrders(LocalDate startDate, LocalDate endDate, List<String> handlersName, List<String> subHandlers);
    List<EmployeeOutputDtoReport> findEmployeeByReports(LocalDate startDateRegistration,
                                                        LocalDate endDateRegistration,
                                                        Integer doneWorksStart,
                                                        Integer doneWorksEnd,
                                                        Integer offerSentStart,
                                                        Integer  offerSentEnd);
    List<CustomerOutputDtoForReport> findCustomerByReports(LocalDate startDate, LocalDate endDate, Integer doneOrderStart, Integer doneOrderEnd);
}
