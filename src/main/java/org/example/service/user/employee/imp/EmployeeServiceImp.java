package org.example.service.user.employee.imp;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.domain.*;
import org.example.dto.EmployeeSignUpDto;
import org.example.dto.OfferDto;
import org.example.enumirations.EmployeeState;
import org.example.enumirations.OrderState;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.OfferPriceIsLessThanOrderPrice;
import org.example.exeptions.OrderStateIsNotCorrect;
import org.example.exeptions.TimeOfWorkDoesntMatch;
import org.example.repository.user.BaseUserRepository;
import org.example.repository.user.employee.EmployeeRepository;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
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
import java.util.Set;
@Service
@Validated

public class EmployeeServiceImp extends BaseUserServiceImp<Employee> implements EmployeeService {
    private final EmployeeRepository employeeRepository ;
    private final OrderService orderService ;
    private final OfferService offerService ;
    //4

    public EmployeeServiceImp(BaseUserRepository baseUserRepository, EmployeeRepository employeeRepository, OrderService orderService, OfferService offerService) {
        super(baseUserRepository);
        this.employeeRepository = employeeRepository;
        this.orderService = orderService;
        this.offerService = offerService;
    }

    @Override
    @Transactional
    @SneakyThrows
    public EmployeeSignUpDto signUpEmployee( EmployeeSignUpDto employeeSignUpDto) {
        File file = new File(employeeSignUpDto.imagePath());
        if (validateEmployee(employeeSignUpDto,file)) {
            Employee employee = new Employee();
            employee.setName(employeeSignUpDto.name());
            employee.setEmail(employeeSignUpDto.email());
            employee.setPhone(employeeSignUpDto.phone());
            employee.setLast_name(employeeSignUpDto.last_name());
            if (validateEmployee(employeeSignUpDto, file)) {
                addImageToEmployee(employee, file);
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
            employee.setPassAndUser(passAndUser);
            saveEmployee(employee);
            return employeeSignUpDto;
        }
        return null;
    }
    //3
    @SneakyThrows
    @Override
    public OfferDto GiveOfferToOrder(OfferDto offerDto)
    {
        Employee employee = findById(offerDto.employeeId(),Employee.class);
        Orders orders = orderService.findById(offerDto.orderId());
        if (validateIfItCanGetOffer(orders)) {
            Offer offer = new Offer();
            offer.setEmployee(employee);
            offer.setTimeOfCreate(LocalDateTime.now());
            if (offerDto.offerPrice()<orders.getOfferedPrice()){
                throw new OfferPriceIsLessThanOrderPrice();
            }
            offer.setOfferPrice(offerDto.offerPrice());
            if (offerDto.timeOfWork().isAfter(orders.getTimeOfWork())) {
                offer.setTimeOfWork(offerDto.timeOfWork());
            } else {
                throw new TimeOfWorkDoesntMatch();
            }
            offer.setWorkTimeInMinutes(offerDto.workTimeInMinutes());
            orders.setOrderState(OrderState.UNDER_CHOOSING_EMPLOYEE);
            orderService.update(orders);
            offer.setOrders(orders);
            offerService.save(offer);
            return offerDto;
        }
        return null;
    }
    @SneakyThrows
    @Override
    public boolean validateIfItCanGetOffer(Orders orders){
        if (orders.getOrderState().equals(OrderState.WAITING_FOR_EMPLOYEE_OFFER) ||
                orders.getOrderState().equals(OrderState.UNDER_CHOOSING_EMPLOYEE)
        ) {
            return true;
        }else {
            throw new OrderStateIsNotCorrect();
        }
    }
    //2
    @SneakyThrows
    @Transactional
    public void saveEmployee( Employee employee){
         employeeRepository.save(employee);
    }
    @SneakyThrows
    @Override
    public  boolean validateImage(File imageFile) {
        try {
            ImageIO.read(imageFile);
            return true;
        }catch (Exception e) {
            throw new Exception("cant read file!");
        }
    }

    @SneakyThrows
    @Override
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
    //1
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
