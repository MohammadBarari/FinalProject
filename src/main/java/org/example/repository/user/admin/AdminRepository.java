package org.example.repository.user.admin;

import org.example.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AdminRepository extends JpaRepository<Employee, Integer> {
    @Modifying
    @Query(value = """
        delete from employee_sub_handlers where employee_id = ?1
    and sub_handlers_id = ?2
""",nativeQuery = true)
    void deleteEmployeeFromSubHandler(Integer employeeId,Integer subHandlerId);
}
