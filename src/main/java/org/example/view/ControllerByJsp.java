package org.example.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ControllerByJsp {
    @GetMapping("/chargeCredit/{customerId}")
    public String getCustomerChargeCreditForm(@PathVariable Integer customerId, Model model) {
        model.addAttribute("customerId", customerId);
        return "ControllerView";
    }
}
