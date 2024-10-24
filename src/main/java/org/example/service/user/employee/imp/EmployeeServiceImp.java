package org.example.service.user.employee.imp;
import org.example.domain.*;
import org.example.dto.EmployeeSignUpDto;
import org.example.dto.admin.EmployeeInputHandlersDto;
import org.example.dto.admin.EmployeeOutputDtoHandlers;
import org.example.dto.admin.EmployeeOutputDtoReport;
import org.example.dto.admin.FindFilteredEmployeeDto;
import org.example.dto.employee.EmployeeLoginDtoOutput;
import org.example.dto.employee.OfferDto;
import org.example.dto.employee.OrderOutputEmployee;
import org.example.dto.employee.SubHandlerOutput;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.dto.subHandlers.SubHandlersDtoOutput;
import org.example.dto.user.OrdersOutputDtoUser;
import org.example.enumirations.EmployeeState;
import org.example.enumirations.OrderState;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.*;
import org.example.repository.user.BaseUserRepository;
import org.example.repository.user.employee.EmployeeRepository;
import org.example.service.credit.CreditService;
import org.example.service.emailToken.EmailTokenService;
import org.example.service.mapStruct.EntityMapper;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.user.BaseUserServiceImp;
import org.example.service.user.employee.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmployeeServiceImp extends BaseUserServiceImp<Employee> implements EmployeeService {
    private final EmployeeRepository employeeRepository ;
    public EmployeeServiceImp(BaseUserRepository baseUserRepository, CreditService creditService, EmailTokenService emailTokenService, EntityMapper entityMapper, EmployeeRepository employeeRepository, OrderService orderService, OfferService offerService, SubHandlerService subHandlerService) {
        super(baseUserRepository,creditService,orderService,offerService,subHandlerService,entityMapper,emailTokenService);
        this.employeeRepository = employeeRepository;
    }
    @Override
    @Transactional
    public EmployeeSignUpDto signUpEmployee(EmployeeSignUpDto employeeSignUpDto) throws Exception {

        if (validateEmployee(employeeSignUpDto,employeeSignUpDto.imageBase64())) {
            Employee employee = getEmployee(employeeSignUpDto,employeeSignUpDto.imageBase64());
            Credit credit = Credit.builder().typeOfEmployee(TypeOfUser.EMPLOYEE).amount(0.0d).build();
            employee.setCredit(credit);
            PassAndUser passAndUser = getPassAndUser(employeeSignUpDto);
            employee.setPassAndUser(passAndUser);
            sendToken(employeeSignUpDto.email(),TypeOfUser.EMPLOYEE);
            saveEmployee(employee);
            return employeeSignUpDto;
        }
        throw new InvalidEmployeeDataException("Invalid input data for employee sign up");
    }

    private static PassAndUser getPassAndUser(EmployeeSignUpDto employeeSignUpDto) {
        return PassAndUser.builder()
                .pass(employeeSignUpDto.password())
                .typeOfUser(TypeOfUser.EMPLOYEE)
                .username(employeeSignUpDto.phone()).build();
    }

    private Employee getEmployee(EmployeeSignUpDto employeeSignUpDto, String file) throws Exception {
        Employee employee =entityMapper.dtoToEmployee(employeeSignUpDto);
        employee.setImage(decodeImage(file));
        employee.setEmployeeState(EmployeeState.NEW);
        employee.setTimeOfRegistration(LocalDateTime.now());
        employee.setScore(0);
        return employee;
    }

    @Override
    public OfferDto giveOfferToOrder(OfferDto offerDto)
    {
        checkemployeeExists(offerDto);
        Orders orders = Optional.ofNullable(orderService.findById(offerDto.orderId())).orElseThrow(()-> new NotFoundOffer("Unable to find order with this ID : " + offerDto.orderId()));
        checkForOffering(offerDto,orders);
        Offer offer = entityMapper.dtoToOffer(offerDto);
        Employee employee = new Employee();
        employee.setId(offerDto.employeeId());
        offer.setEmployee(employee);
        offer.setTimeOfCreate(LocalDateTime.now());
        orders.setOrderState(OrderState.UNDER_CHOOSING_EMPLOYEE);
        offer.setOrders(orders);
        orderService.update(orders);
        offerService.save(offer);
        return offerDto;
    }
    private void checkemployeeExists(OfferDto offerDto) {
        if (!employeeExistsByEmployeeId(offerDto.employeeId())){
            throw new NotFoundEmployee();
        }
    }
    private void checkForOffering(OfferDto offerDto ,Orders orders){
        if (!(orders.getOrderState().equals(OrderState.WAITING_FOR_EMPLOYEE_OFFER) ||
                orders.getOrderState().equals(OrderState.UNDER_CHOOSING_EMPLOYEE))
        ) {
            throw new OrderStateIsNotCorrect();
        }
        if (!offerDto.timeOfWork().isAfter(orders.getTimeOfWork())){
            throw new TimeOfWorkDoesntMatch();
        }
        if (offerDto.offerPrice()<orders.getOfferedPrice()){
            throw new OfferPriceIsLessThanOrderPrice();
        }
    }
    //2
    @Transactional
    public void saveEmployee( Employee employee){
        employeeRepository.save(employee);
    }
    @Override
    public  boolean validateImage(File imageFile){
        try {
            if (ImageIO.read(imageFile) == null) {
                throw new ImageValidationException("The file is not a valid image.");
            }
            return true;
        } catch (IOException e) {
            throw new ImageValidationException("Cannot read file: " + imageFile.getName(), e);
        }
    }
    @Override
    public  boolean checkIfImageSizeIsOkay(File imageFile) {
        if (imageFile.length() < 300*1024){
            return true;
        }
        throw new RuntimeException("file is too large");
    }

    @Override
    public List<SubHandlerOutput> findAllSubHandlersForEmployee(Integer employeeId) {
        List<SubHandler> subHandlers =  subHandlerService.subHandlersForEmployee(employeeId);
        List<SubHandlerOutput> subHandlerOutputs = new ArrayList<>();
        subHandlers.forEach(subHandler -> {
            SubHandlerOutput s = entityMapper.subHandlerToDto(subHandler);
            subHandlerOutputs.add(s);
        });
        return subHandlerOutputs;
    }

    @Transactional
    @Override
    public List<OrderOutputEmployee> getOrdersForEmployee(Integer employeeId) {
        List<Orders> orders = new ArrayList<>();
        List<SubHandler> subHandlers = subHandlerService.subHandlersForEmployee(employeeId);
        if (subHandlers!=null && !subHandlers.isEmpty()) {
            for (SubHandler subHandler : subHandlers) {
                List<Orders> ordersPerSubHandler = orderService.findOrdersForSubHandler(subHandler.getId());
                ordersPerSubHandler.forEach(orders1 -> System.out.println(orders1.getCustomer().getId()));
                if (!orderService.findOrdersForSubHandler(subHandler.getId()).isEmpty()) {
                    if (!orders.isEmpty()) {
                        orders.addAll(orderService.findOrdersForSubHandler(subHandler.getId()));
                    } else {
                        orders.addAll(ordersPerSubHandler);
                    }
                }
            }
        }
        List<OrderOutputEmployee> orderOutputEmployees = new ArrayList<>();
        orders.forEach(orders1 -> {
            OrderOutputEmployee o = new OrderOutputEmployee(orders1.getId(),orders1.getOfferedPrice(),orders1.getDetail(),orders1.getSubHandler().getName(),orders1.getTimeOfWork(),orders1.getAddress(),orders1.getOrderState(),orders1.getCustomer().getName(),orders1.getCustomer().getId());
            orderOutputEmployees.add(o);
        });
        return orderOutputEmployees;
    }

    @Override
    public Boolean employeeExistsByEmployeeId(Integer employeeId) {
        return employeeRepository.employeeExistsById(employeeId);
    }

    @Transactional
    @Override
    public List<EmployeeOutputDtoHandlers> findEmployeesByOptionalInformation(EmployeeInputHandlersDto input) {
        List<Employee> employees =  employeeRepository.selectEmployeesByOptionalInformation(input);
        List<EmployeeOutputDtoHandlers> employeeOutputDtoHandlers = new ArrayList<>();
        if (employees!=null && !employees.isEmpty()) {
            for (Employee employee : employees) {
                List<String> subHandlersNameString = new ArrayList<>();
                if (employee.getSubHandlers() != null && !employee.getSubHandlers().isEmpty()) {
                    for (SubHandler subHandler : employee.getSubHandlers()) {
                        subHandlersNameString.add("name : " + subHandler.getName() + "||| handlerName : " + subHandler.getHandler().getName());
                    }
                }
                employeeOutputDtoHandlers.add(new EmployeeOutputDtoHandlers(employee.getId(),employee.getName(),employee.getLast_name(),employee.getEmail(),employee.getPhone(),employee.getTimeOfRegistration(),employee.getEmployeeState(),employee.getScore(),subHandlersNameString));
            }
        }
        return employeeOutputDtoHandlers;
    }

    @Override
    public void setUnderReviewState(String email) {
        employeeRepository.SetUnderReviewState(email);
    }

    @Override
    public Boolean employeeExistsByEmail(String email) {
        return employeeRepository.employeeExistsByEmail(email);
    }

    private void addImageToEmployee(Employee employee, File file) throws IOException {
        if (file.exists() && file.length() > 0) {
            try (InputStream inputStream = new FileInputStream(file)) {
                byte[] photo = new byte[(int) file.length()];
                int bytesRead = inputStream.read(photo);
                if (bytesRead > 0) {
                    employee.setImage(photo);
                } else {
                    throw new IOException("Failed to read the image data from file: " + file.getName());
                }
            } catch (IOException e) {
                throw new IOException("Error while reading image file: " + file.getName(), e);
            }
        } else {
            throw new IOException("File does not exist or is empty: " + file.getAbsolutePath());
        }
    }

    @Override
    @Transactional
    public List<OrderOutputEmployee> findOrdersForEmployee(Integer employeeId) {
        List<Orders> orders = new ArrayList<>();
        List<SubHandler> subHandlers =subHandlerService.subHandlersForEmployee(employeeId);
        if (subHandlers!=null && !subHandlers.isEmpty()) {
            for (SubHandler subHandler : subHandlers) {
                List<Orders> ordersPerSubHandler = orderService.findOrdersForSubHandler(subHandler.getId());
                if (!ordersPerSubHandler.isEmpty()) {
                    for (Orders order : ordersPerSubHandler) {
                        if (order.getOrderState().equals(OrderState.UNDER_CHOOSING_EMPLOYEE) || order.getOrderState().equals(OrderState.WAITING_FOR_EMPLOYEE_OFFER)) {
                            orders.add(order);
                        }
                    }
                }
            }
        }
        List<OrderOutputEmployee> orderOutputEmployees = new ArrayList<>();
        orders.forEach(orders1 -> {
            OrderOutputEmployee o = new OrderOutputEmployee(orders1.getId(),orders1.getOfferedPrice(),orders1.getDetail(),orders1.getSubHandler().getName(),orders1.getTimeOfWork(),orders1.getAddress(),orders1.getOrderState(),orders1.getCustomer().getName(),orders1.getCustomer().getId());
            orderOutputEmployees.add(o);
        });
        return orderOutputEmployees;
    }
    @Override
    public boolean validateEmployee(EmployeeSignUpDto employee, String file) throws IOException {
        return validatePassWord(employee.password()) && checkIfNotDuplicateUser(employee.phone())
                && validateImageJpg (file) && checkImageSize(file);
    }
    @Override
    public EmployeeLoginDtoOutput login(String user, String pass)  {
        Employee employee =  Optional.ofNullable(employeeRepository.login(user,pass)).orElseThrow(() -> new NotFoundEmployee("username or password maybe incorrect"));
        return new EmployeeLoginDtoOutput(employee.getId(),employee.getName(),employee.getLast_name(),employee.getEmail(),employee.getPhone(),employee.getCredit().getAmount(),employee.getImage(),employee.getScore());
    }
    @Override
    public boolean checkIfNotDuplicateUser(String user) {
        return Objects.isNull(employeeRepository.find(user, Employee.class));
    }
    @Override
    public void sendToken(String email , TypeOfUser typeOfUser) {
        emailTokenService.sendEmail(email,typeOfUser);
    }
    @Override
    @Transactional
    public String validateEmployeeEmail(String token) {
        EmailToken emailToken = Optional.ofNullable(emailTokenService.findByToken(token)).orElseThrow(()-> new InvalidTokenExceptions("not found token"));
        emailTokenService.validateToken(token);
        validateEmployeeToken(emailToken);
        return "successful";
    }

    private void validateEmployeeToken(EmailToken emailToken) {
        if (employeeExistsByEmail(emailToken.getEmail())){
            setUnderReviewState(emailToken.getEmail());
            emailToken.setExpired(true);
        }
    }
    @Override
    public List<Orders> findPaidOrders(Integer employeeId) {
        return orderService.findPaidOrdersForEmployee(employeeId);
    }

    @Override
    @Transactional
    public List<DoneDutiesDto> findDoneWorksById(Integer id) {
        List<DoneDutiesDto> doneDutiesDtos = new ArrayList<>();
        List<Orders> allDoneOrders = orderService.findPaidOrdersForEmployee(id);
        if (allDoneOrders!=null && !allDoneOrders.isEmpty()) {
            for (Orders orders : allDoneOrders) {
                if (orders.getOrderState() == OrderState.PAID) {
                    Offer offer = Optional.ofNullable(offerService.findAcceptedOfferInOrder(orders.getId())).orElseThrow(() -> new NotFoundOffer("offer not found"));
                    DoneDutiesDto dto = getDoneDutiesDto(id, orders, offer);
                    doneDutiesDtos.add(dto);
                }
            }
        }
        return doneDutiesDtos;
    }

    private  DoneDutiesDto getDoneDutiesDto(Integer id, Orders orders, Offer offer) {
        String employeeName = orders.getEmployee().getName() + " " + orders.getEmployee().getLast_name();
        Integer customerId = orders.getCustomer().getId();
        String customerFullName = orders.getCustomer().getName() + " " + orders.getCustomer().getLast_name();
        Double price = Double.valueOf(offer.getOfferPrice());
        DoneDutiesDto dto = new DoneDutiesDto(orders.getTimeOfWork(), price, new SubHandlersDtoOutput(orders.getSubHandler().getName(), orders.getSubHandler().getDetail(), orders.getSubHandler().getBasePrice()), orders.getScore(), id, employeeName, customerFullName, customerId, orders.getComment());
        return dto;
    }

    @Override
    public List<EmployeeOutputDtoReport> findEmployeeByReports(FindFilteredEmployeeDto input) {
        return employeeRepository.selectEmployeeByReports(input.startDateRegistration(),input.endDateRegistration(),input.doneWorksStart(),input.doneWorksEnd(),input.offerSentStart(),input.offerSentEnd());
    }

    @Override
    public List<OrdersOutputDtoUser> optionalSelectOrdersForEmployee(Integer employeeId, String orderState) {
        List<Orders> orders = orderService.optionalFindOrdersForEmployee(employeeId, orderState);
        List<OrdersOutputDtoUser> ordersOutputDtoUsers = new ArrayList<>();
        for (Orders orders1 : orders) {
            ordersOutputDtoUsers.add(new OrdersOutputDtoUser(orders1.getId(),orders1.getOfferedPrice(), orders1.getDetail(), orders1.getTimeOfWork(), orders1.getAddress(), orders1.getOrderState(), orders1.getScore(), orders1.getComment()));
        }
        return ordersOutputDtoUsers;
    }

    @Override
    public Double getCreditAmount(Integer id) {
        return Optional.ofNullable(creditService.findByEmployeeId(id).getAmount()).orElseThrow(()-> new NotFoundEmployee("employee not found with id: "+id ) );
    }

    private boolean validateImageJpg(String file) throws IOException {
        byte[] imageBytes = decodeImage(file);
        // Check if the image has enough bytes to be a JPEG
        if (imageBytes.length < 4) { // At least 4 bytes needed for header and footer
            throw new ItIsNotJpgFile("File is too short to be a valid image");
        }

        // Check for JPEG signature (FF D8 at start and FF D9 at end)
        if (imageBytes[0] != (byte) 0xFF || imageBytes[1] != (byte) 0xD8) {
            throw new ItIsNotJpgFile("The file is not a JPG image");
        }

        if (imageBytes[imageBytes.length - 2] != (byte) 0xFF || imageBytes[imageBytes.length - 1] != (byte) 0xD9) {
            throw new ItIsNotJpgFile("The file is not a valid JPG image (missing footer)");
        }
        return true;
    }
    private boolean checkImageSize (String file) throws IOException {
        long sizeInKb = decodeImage(file).length /1024;
        return sizeInKb < 300;
    }
    public byte[] decodeImage(String base64Image) {
        try {
            return Base64.getDecoder().decode(base64Image);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 input", e);
        }
    }
}
