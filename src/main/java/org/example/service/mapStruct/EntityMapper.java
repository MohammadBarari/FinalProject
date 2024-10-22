package org.example.service.mapStruct;

import org.example.domain.*;
import org.example.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    CustomerSignUpDto customerToDto(Customer customer);

    Customer dtoToCustomer(CustomerSignUpDto customerDto);

    OrderDto orderToDto(Orders order);

    Orders dtoToOrder(OrderDto orderDto);

    Employee dtoToEmployee(EmployeeSignUpDto employeeSignUpDto);

    EmployeeSignUpDto employeeToDto(Employee employee);

    Offer dtoToOffer(OfferDto offerDto);

    SubHandler dtoToSubHandler(SubHandlerDto subHandler);
}
