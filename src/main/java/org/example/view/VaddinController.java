package org.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.example.dto.PayToCartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Route("customer-charge")
public class VaddinController extends VerticalLayout {
    private final RestTemplate restTemplate;

    @Autowired
    public VaddinController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        TextField customerIdField = new TextField("Customer ID");
        NumberField amountField = new NumberField("Amount");
        amountField.setPlaceholder("Enter amount");
        amountField.setMin(0.0);
        TextField cartNumberField = new TextField("Cart Number (16 digits)");
        cartNumberField.setPlaceholder("Enter cart number");
        TextField cvvField = new TextField("CVV (3 digits)");
        cvvField.setPlaceholder("Enter CVV");
        DatePicker expiresDateField = new DatePicker("Expires Date");
        Button submitButton = new Button("Charge Credit");
        submitButton.addClickListener(event -> {
            handleChargeCredit(
                    customerIdField.getValue(),
                    amountField.getValue(),
                    cartNumberField.getValue(),
                    cvvField.getValue(),
                    expiresDateField.getValue()
            );
        });

        add(customerIdField, amountField, cartNumberField, cvvField, expiresDateField, submitButton);
    }

    private void handleChargeCredit(String customerId, Double amount, String cartNumber, String cvv, LocalDate expiresDate) {
        // Validate inputs
        if (customerId == null || customerId.isEmpty() || amount == null || cartNumber == null || cartNumber.isEmpty() ||
                cvv == null || cvv.isEmpty() || expiresDate == null) {
            Notification.show("Please fill in all fields correctly.", 3000, Notification.Position.MIDDLE);
            return;
        }
        try {
            PayToCartDto dto = new PayToCartDto(
                    Integer.parseInt(customerId),
                    amount,
                    cartNumber,
                    cvv,
                    expiresDate
            );

            String response = restTemplate.postForObject("http://localhost:8080/customer/customerChargeCredit", dto, String.class);
            Notification.show("Response: " + response);
        } catch (NumberFormatException e) {
            Notification.show("Invalid input for Customer ID or Amount.", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
