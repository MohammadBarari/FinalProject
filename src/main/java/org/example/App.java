package org.example;

import org.example.domain.*;
import org.example.dto.OfferDto;
import org.example.dto.OrderDto;
import org.example.service.mainService.PaymentService;
import org.example.service.mainService.imp.CommentService;
import org.example.service.mainService.imp.GetSubHandlerForCustomerService;
import org.example.service.mainService.imp.PaymentServiceImp;
import org.example.service.order.OrderService;
import org.example.service.order.imp.OrderServiceImp;
import org.example.service.user.admin.AdminService;
import org.example.service.user.admin.imp.AdminServiceImp;
import org.example.service.user.customer.CustomerService;
import org.example.service.user.customer.imp.CustomerServiceImp;
import org.example.service.user.employee.EmployeeService;
import org.example.service.user.employee.imp.EmployeeServiceImp;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        CustomerService customerService = new CustomerServiceImp();
//        CustomerSignUpDto customerSignUpDto = new CustomerSignUpDto("Mohammad","Barari","mbarari258@gmail.com","09335212399","1234567h");
//        customerService.signUpCustomer(customerSignUpDto);
        EmployeeService employeeService = new EmployeeServiceImp();
//        EmployeeSignUpDto employeeSignUpDto = new EmployeeSignUpDto("Aghar","Asghari","asghari@gmail.com","09127205457","1234gj21",
//                "C:\\Users\\nohammad\\Desktop\\salam\\photo_2024-09-10_16-46-11.jpg");
//        employeeService.signUpEmployee(employeeSignUpDto);
//        Handler handler = new Handler();
//        handler.setName("Housing service");
//        HandlerService handlerService = new HandlerBaseImp();
//        handlerService.save(handler);
////
////        //SubHandler subHandler = new SubHandler();
////        List<Handler> findAllHandler = handlerService.findAllHandlers();
////        findAllHandler.forEach(handler -> {
////            System.out.println(handler.getName());
////        });
//        Handler handler = handlerService.findHandlerById(1);
//        SubHandler subHandler = new SubHandler();
//        subHandler.setName("pipeService");
//        subHandler.setDetail("fixing your pipe");
//        subHandler.setHandler(handler);
//        SubHandlerService subHandlerService = new SubHandlerServiceImp();
//        subHandlerService.saveSubHandler(subHandler);
////
        Employee employee = employeeService.findById(1,Employee.class);
        AdminService adminService = new AdminServiceImp();
//        adminService.changeEmployeeState(1, EmployeeState.ACCEPTED);
        adminService.saveEmployeeToSubHandler(employee,1);
//        adminService.removeEmployeeFromSubHandler(employee,1);
        OrderService orderService = new OrderServiceImp();
        Customer customer = customerService.findById(2,Customer.class);
        GetSubHandlerForCustomerService getSubHandlerForCustomerService = new GetSubHandlerForCustomerService();
        OrderDto orderDto = new OrderDto(23000000.0,"zoodBia", LocalDateTime.now().plusHours(3),"koocheyeMadai",1,1);
//        getSubHandlerForCustomerService.GetSubHandlerForCustomerService(1,customer,orderDto);
        OfferDto offerDto= new OfferDto(400000000L,LocalDateTime.now().plusHours(6) ,50,1,1);
//        getSubHandlerForCustomerService.GiveOfferToOrder(4,employee,offerDto);
//        getSubHandlerForCustomerService.customerAcceptOffer(2);
//        getSubHandlerForCustomerService.setOrderStateToStart(4, OrderState.DONE);
        PaymentService paymentService = new PaymentServiceImp();
//        paymentService.customerChargeCart(customer,500000000);

//        paymentService.customerPayOrder(4,customer);
        CommentService commentService= new CommentService();
        commentService.giveComment(4,4,"kheli khoob boodd");
    }
}
