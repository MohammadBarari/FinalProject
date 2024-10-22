package org.example.service.mapStruct;

import org.example.domain.Customer;
import org.example.domain.Orders;
import org.example.dto.CustomerSignUpDto;
import org.example.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    CustomerSignUpDto customerToDto(Customer customer);

    Customer dtoToCustomer(CustomerSignUpDto customerDto);

    OrderDto orderToDto(Orders order);

    Orders dtoToOrder(OrderDto orderDto);



}
