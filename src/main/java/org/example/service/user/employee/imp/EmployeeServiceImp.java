package org.example.service.user.employee.imp;

import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.domain.Credit;
import org.example.domain.Employee;
import org.example.domain.PassAndUser;
import org.example.dto.EmployeeSignUpDto;
import org.example.enumirations.EmployeeState;
import org.example.enumirations.TypeOfUser;
import org.example.repository.user.employee.EmployeeRepository;
import org.example.service.user.BaseUserServiceImp;
import org.example.service.user.employee.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Objects;
@Service
@Validated
@AllArgsConstructor
public class EmployeeServiceImp extends BaseUserServiceImp<Employee> implements EmployeeService {
    private final EmployeeRepository employeeRepository ;
    private final Validator validator;
    @Override
    public void signUpEmployee( EmployeeSignUpDto employeeSignUpDto) throws IOException {
        File file = new File(employeeSignUpDto.imagePath());
        if (validateEmployee(employeeSignUpDto,file)) {
           @Valid Employee employee = new Employee();
            employee.setName(employeeSignUpDto.name());
            employee.setEmail(employeeSignUpDto.email());
            employee.setPhone(employeeSignUpDto.phone());
            employee.setLast_name(employeeSignUpDto.last_name());
            if (validateEmployee(employeeSignUpDto,file)){
                addImageToEmployee(employee,file);
            }
            employee.setTimeOfRegistration(LocalDateTime.now());

            employee.setScore(0);

            Credit credit = new Credit();

            credit.setTypeOfEmployee(TypeOfUser.EMPLOYEE);

            credit.setAmount(0.0);
            employee.setCredit(credit);
            PassAndUser passAndUser = PassAndUser.builder()
                    .pass(employeeSignUpDto.password())
                    .typeOfUser(TypeOfUser.EMPLOYEE)
                    .username(employeeSignUpDto.phone()).build();
            employee.setEmployeeState(EmployeeState.NEW);
            saveEmployee(employee,passAndUser);
        }
    }
    @SneakyThrows
    public void saveEmployee(@Valid Employee employee, @Valid PassAndUser passAndUser){
        validator.validate(passAndUser);
        validator.validate(employee);
        employee.setPassAndUser(passAndUser);
        employeeRepository.save(employee,passAndUser);
    }
    @SneakyThrows
    public  boolean validateImage(File imageFile) {
        try {
            ImageIO.read(imageFile);
            return true;
        }catch (Exception e) {
            throw new Exception("cant read file!");
        }
    }
    @SneakyThrows
    public  boolean checkIfImageSizeIsOkay(File imageFile) {
        if (imageFile.length() < 300*1024){
            return true;
        }
        throw new Exception("file is too large");
    }
    private void addImageToEmployee(Employee employee, File file) throws IOException {
        if (file.exists()) {
            InputStream inputStream = new FileInputStream(file);
            byte[] photo = new byte[(int) file.length()];
            inputStream.read(photo);
            employee.setImage(photo);
        }
    }
    @SneakyThrows
    @Override
    public boolean validateEmployee(EmployeeSignUpDto employee, File file) {
        //todo: validate employee
        if (validatePassWord(employee.password())&& checkIfNotDuplicateUser(employee.phone())
        && checkIfImageSizeIsOkay(file) && validateImage(file)) {
            return true;
        }

        return false;
    }

    @Override
    public Employee login(String user, String pass) {
        return employeeRepository.login(user,pass);
    }

    @Override
    public boolean checkIfNotDuplicateUser(String user) {
        if (Objects.isNull(employeeRepository.find(user,Employee.class)))
        {
            return true;
        }
        return false;
    }


}
