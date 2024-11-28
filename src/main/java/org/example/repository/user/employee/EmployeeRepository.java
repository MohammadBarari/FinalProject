package org.example.repository.user.employee;

import org.example.domain.Employee;
import org.example.dto.admin.EmployeeInputHandlersDto;
import org.example.dto.admin.EmployeeOutputDtoReport;
import org.example.repository.user.BaseUserRepository;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends BaseUserRepository<Employee> {
    Employee login (String username, String password);

    Boolean employeeExistsById(Integer id);

    List<Employee> selectEmployeesByOptionalInformation(EmployeeInputHandlersDto input);

    void SetUnderReviewState(String email);

    Boolean employeeExistsByEmail(String mail);

    List<EmployeeOutputDtoReport> selectEmployeeByReports(LocalDate startDateRegistration,
                                                          LocalDate endDateRegistration,
                                                          Integer doneWorksStart,
                                                          Integer doneWorksEnd,
                                                          Integer offerSentStart,
                                                          Integer  offerSentEnd);

    Employee findByUser(String user);

}
