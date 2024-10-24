package org.example.service.user.admin;

import org.example.domain.Customer;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.dto.ChangeSubHandlerDto;
import org.example.dto.SubHandlerDto;
import org.example.dto.admin.*;
import org.example.dto.orders.OrderOutputDto;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.enumirations.TypeOfUser;

import java.util.List;

public interface AdminService {
    Handler saveHandler(String handlerName) ;
    SubHandler saveSubHandler(SubHandlerDto subHandlerDto);
    void saveEmployeeToSubHandler(Integer employeeId,Integer subHandlerId);
    void removeEmployeeFromSubHandler(Integer employeeId, Integer subHandlerId) ;
    void acceptEmployee(Integer employeeId);
    void detailPriceSubHandlerChanger(ChangeSubHandlerDto changeSubHandlerDto);
    List<Customer> findCustomerByOptional(FindFilteredCustomerDto input);
    List<EmployeeOutputDtoHandlers> findEmployeesByOptionalInformation(EmployeeInputHandlersDto input);
    List<DoneDutiesDto> findPaidWorksById(Integer id, TypeOfUser typeOfUser);
    List<OrderOutputDto> optionalFindOrders(FindFilteredOrdersDto input);
    List<EmployeeOutputDtoReport> findEmployeeByReports(FindFilteredEmployeeDto input);
    List<CustomerOutputDtoForReport> findCustomerByReports(FindCustomerByFilterDto input);
}
