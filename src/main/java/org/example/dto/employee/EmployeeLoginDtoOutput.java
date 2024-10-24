package org.example.dto.employee;

public record EmployeeLoginDtoOutput(Integer id,String firstName,String lastName,String email,String phone,Double creditMoney,byte[] image,Integer score) {
}
