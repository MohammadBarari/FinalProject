package org.example.dto.admin;

public record FindFilteredCustomerDto(String name,
                                      String lastName,
                                      String email ,
                                      String phone) {
}
