package org.example.service.mapStruct;

import org.example.domain.*;
import org.example.dto.CustomerSignUpDto;
import org.example.dto.EmployeeSignUpDto;
import org.example.dto.OrderDto;
import org.example.dto.SubHandlerDto;
import org.example.dto.admin.CustomerOutput;
import org.example.dto.customer.OrderDtoInput;
import org.example.dto.employee.OfferDto;
import org.example.dto.employee.OrderOutputEmployee;
import org.example.dto.employee.SubHandlerOutput;
import org.example.dto.orders.OrderOutputDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    OrderOutputDto toOrderOutputDto(Orders order);

    OrderOutputEmployee toOrderOutputEmployee(Orders order);

    CustomerSignUpDto customerToDto(Customer customer);

    SubHandlerOutput subHandlerToDto(SubHandler subHandler);

    Customer dtoToCustomer(CustomerSignUpDto customerDto);

    CustomerOutput customerToDtoAdmin(Customer customer);

    OrderDto orderToDto(Orders order);

    Orders dtoToOrder(OrderDto orderDto);

    Employee dtoToEmployee(EmployeeSignUpDto employeeSignUpDto);

    EmployeeSignUpDto employeeToDto(Employee employee);

    Offer dtoToOffer(OfferDto offerDto);

    SubHandler dtoToSubHandler(SubHandlerDto subHandler);

    OrderDto inputToDto(OrderDtoInput orderDtoInput);
}
