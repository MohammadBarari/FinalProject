package org.example.controller.user.admin;

import lombok.RequiredArgsConstructor;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.dto.SaveSubHandlerDto;
import org.example.service.user.admin.AdminService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/saveHandler")
    public Handler saveHandler(@RequestBody String name){
        return adminService.saveHandler(name);
    }

    @GetMapping("/saveSubHander")
    public SubHandler saveSubHandler(@RequestBody SaveSubHandlerDto subHandlerDto){
        return adminService.saveSubHandler(subHandlerDto);
    }

    @GetMapping("/saveEmployeeToSubHandler/{employeeId}/{subHandlerId}")
    public void saveEmployeeToSubHandler(Integer employeeId, Integer subHandlerId){
        adminService.saveEmployeeToSubHandler(employeeId, subHandlerId);
    }
    @GetMapping("/removeEmployeeFromSubHandler/{employeeId}/{subHandlerId}")
    public void removeEmployeeFromSubHandler(@PathVariable Integer employeeId
            ,@PathVariable Integer subHandlerId){
        adminService.removeEmployeeFromSubHandler(employeeId,subHandlerId);
    }

    @GetMapping("/validateTheEmployee/{employeeId}")
    public void validateTheEmployee(@PathVariable("employeeId") Integer employeeId){
        adminService.validateTheEmployee(employeeId);
    }
}
