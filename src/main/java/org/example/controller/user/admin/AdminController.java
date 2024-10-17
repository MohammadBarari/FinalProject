package org.example.controller.user.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.dto.ChangeSubHandlerDto;
import org.example.dto.SaveSubHandlerDto;
import org.example.service.user.admin.AdminService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/saveHandler/{name}")
    public Handler saveHandler(@PathVariable @NotNull  String name){
        return adminService.saveHandler(name);
    }

    @PostMapping("/saveSubHander")
    public SubHandler saveSubHandler(@RequestBody @Valid SaveSubHandlerDto subHandlerDto){
        return adminService.saveSubHandler(subHandlerDto);
    }

    @GetMapping("/saveEmployeeToSubHandler/{employeeId}/{subHandlerId}")
    public void saveEmployeeToSubHandler(@PathVariable @NotNull Integer employeeId,@PathVariable @NotNull Integer subHandlerId){
        adminService.saveEmployeeToSubHandler(employeeId, subHandlerId);
    }
    @GetMapping("/removeEmployeeFromSubHandler/{employeeId}/{subHandlerId}")
    public void removeEmployeeFromSubHandler(@PathVariable @NotNull Integer employeeId
            ,@PathVariable @Digits(integer = 3,fraction = 0) @NotNull Integer subHandlerId){
        adminService.removeEmployeeFromSubHandler(employeeId,subHandlerId);
    }

    @GetMapping("/validateTheEmployee/{employeeId}")
    public void validateTheEmployee(@PathVariable("employeeId") @Digits(integer = 3,fraction = 0) @NotNull
                                        Integer employeeId){
        adminService.validateTheEmployee(employeeId);
    }

    @PostMapping("/updatingSubHandler")
    public void detailPriceSubHandlerChanger(@Valid ChangeSubHandlerDto changeSubHandlerDto){
        adminService.detailPriceSubHandlerChanger(changeSubHandlerDto);
    }


}
