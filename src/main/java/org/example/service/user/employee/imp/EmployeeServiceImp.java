package org.example.service.user.employee.imp;
import lombok.SneakyThrows;
import org.example.domain.Credit;
import org.example.domain.Employee;
import org.example.domain.PassAndUser;
import org.example.dto.EmployeeSignUpDto;
import org.example.enumirations.EmployeeState;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.FileIsInvalid;
import org.example.exeptions.ImageSizeIsOver;
import org.example.repository.user.employee.EmployeeRepository;
import org.example.repository.user.employee.imp.EmployeeRepositoryImp;
import org.example.service.user.BaseUserServiceImp;
import org.example.service.user.employee.EmployeeService;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class EmployeeServiceImp extends BaseUserServiceImp<Employee> implements EmployeeService {
    EmployeeRepository employeeRepository = new EmployeeRepositoryImp();
    @Override
    public void signUpEmployee(EmployeeSignUpDto employeeSignUpDto) throws IOException {
        File file = new File(employeeSignUpDto.imagePath());
        if (validateEmployee(employeeSignUpDto,file)) {
            Employee employee = new Employee();
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
            savePassAndUser(passAndUser);
            employee.setEmployeeState(EmployeeState.NEW);
            employeeRepository.save(employee);
        }
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
        //finished
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
