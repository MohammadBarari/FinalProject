package org.example.service.user.employee.imp;
import lombok.SneakyThrows;
import org.example.domain.Credit;
import org.example.domain.Employee;
import org.example.domain.PassAndUser;
import org.example.dto.EmployeeSignUpDto;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.FileIsInvalid;
import org.example.exeptions.ImageSizeIsOver;
import org.example.repository.user.employee.EmployeeRepository;
import org.example.repository.user.employee.imp.EmployeeRepositoryImp;
import org.example.service.user.BaseUserServiceImp;
import org.example.service.user.employee.EmployeeService;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Objects;

public class EmployeeServiceImp extends BaseUserServiceImp<Employee> implements EmployeeService {
    EmployeeRepository employeeRepository = new EmployeeRepositoryImp();
    @Override
    public void signUpEmployee(EmployeeSignUpDto employeeSignUpDto) throws FileNotFoundException {
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
        }
    }
    @SneakyThrows
    public  boolean validateImage(File imageFile) {
        try {
            BufferedImage image = ImageIO.read(imageFile);
            return true;
        }catch (Exception e) {
            throw new Exception(e);
        }
    }
    @SneakyThrows
    public  boolean checkIfImageSizeIsOkay(File imageFile) {
        if (imageFile.length() < 300_000){
            return true;
        }
        throw new Exception("file is too large");
    }
    private void addImageToEmployee(Employee employee, File file) throws FileNotFoundException {
        if (file.exists()) {
            InputStream inputStream = new FileInputStream(file);
            byte[] photo = new byte[(int) file.length()];
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
