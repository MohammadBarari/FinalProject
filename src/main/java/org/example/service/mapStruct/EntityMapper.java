package org.example.service.mapStruct;

import org.example.domain.*;
import org.example.dto.*;
import org.example.dto.employee.OfferDto;
import org.example.dto.employee.SubHandlerOutput;
import org.example.dto.orders.OrderOutputDto;
import org.example.dto.employee.OrderOutputEmployee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    OrderOutputDto toOrderOutputDto(Orders order);
    OrderOutputEmployee toOrderOutputEmployee(Orders order);
    CustomerSignUpDto customerToDto(Customer customer);

    SubHandlerOutput subHandlerToDto(SubHandler subHandler);

    Customer dtoToCustomer(CustomerSignUpDto customerDto);

    OrderDto orderToDto(Orders order);

    Orders dtoToOrder(OrderDto orderDto);

    Employee dtoToEmployee(EmployeeSignUpDto employeeSignUpDto);

    EmployeeSignUpDto employeeToDto(Employee employee);

    Offer dtoToOffer(OfferDto offerDto);

    SubHandler dtoToSubHandler(SubHandlerDto subHandler);
}
