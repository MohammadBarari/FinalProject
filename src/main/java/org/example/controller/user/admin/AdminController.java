package org.example.controller.user.admin;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.dto.subHandlers.ChangeSubHandlerDto;
import org.example.dto.subHandlers.SubHandlerDto;
import org.example.dto.admin.*;
import org.example.dto.orders.OrderOutputDto;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.enumirations.TypeOfUser;
import org.example.service.user.admin.AdminService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/handlers")
    public Handler createHandler(@RequestParam @NotNull String name) {
        return adminService.saveHandler(name);
    }

    @PostMapping("/subhandlers")
    public SubHandler createSubHandler(@RequestBody @Valid SubHandlerDto subHandlerDto) {
        return adminService.saveSubHandler(subHandlerDto);
    }

    @PostMapping("/subhandlers/{subHandlerId}/employees/{employeeId}")
    public void assignEmployeeToSubHandler(@PathVariable @NotNull Integer employeeId, @PathVariable @NotNull Integer subHandlerId) {
        adminService.saveEmployeeToSubHandler(employeeId, subHandlerId);
    }

    @DeleteMapping("/subhandlers/{subHandlerId}/employees/{employeeId}")
    public void unassignEmployeeFromSubHandler(@PathVariable @NotNull Integer employeeId, @PathVariable @Digits(integer = 3, fraction = 0) @NotNull Integer subHandlerId) {
        adminService.removeEmployeeFromSubHandler(employeeId, subHandlerId);
    }

    @PostMapping("/employees/{employeeId}/validate")
    public void validateEmployee(@PathVariable @Digits(integer = 5, fraction = 0) @NotNull Integer employeeId) {
        adminService.acceptEmployee(employeeId);
    }

    @PutMapping("/subhandlers")
    public void updateSubHandler(@Valid @RequestBody ChangeSubHandlerDto changeSubHandlerDto) {
        adminService.detailPriceSubHandlerChanger(changeSubHandlerDto);
    }

    @PostMapping("/customers")
    public List<CustomerOutput> findCustomers(@RequestBody FindFilteredCustomerDto input) {
        return adminService.findCustomerByOptional(input);
    }

    @PostMapping("/employees")
    public List<EmployeeOutputDtoHandlers> findEmployees(@RequestBody @Valid EmployeeInputHandlersDto input) {
        return adminService.findEmployeesByOptionalInformation(input);
    }

    @GetMapping("/users/{id}/services/{typeOfUser}")
    public List<DoneDutiesDto> findPaidServices(@PathVariable Integer id, @PathVariable TypeOfUser typeOfUser) {
        return adminService.findPaidWorksById(id, typeOfUser);
    }

    @PostMapping("/orders")
    public List<OrderOutputDto> findOrders(@RequestBody @Valid FindFilteredOrdersDto input) {
        return adminService.optionalFindOrders(input);
    }

    @PostMapping("/reports/employees")
    public List<EmployeeOutputDtoReport> getEmployeeReports(@RequestBody @Valid FindFilteredEmployeeDto input) {
        return adminService.findEmployeeByReports(input);
    }

    @PostMapping("/reports/customers")
    public List<CustomerOutputDtoForReport> getCustomerReports(@RequestBody @Valid FindCustomerByFilterDto input) {
        return adminService.findCustomerByReports(input);
    }

    @GetMapping("/greet")
    public String greet() {
        return "hi";
    }
}
