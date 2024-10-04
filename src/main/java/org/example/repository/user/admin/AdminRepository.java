package org.example.repository.user.admin;

import org.example.domain.Employee;
import org.example.domain.Handler;

public interface AdminRepository {
    void saveHandler(Handler handler);
    void deleteEmployeeFromSubHandler(Employee employee,Integer subHandlerId);
}
