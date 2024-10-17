package org.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.example.dto.PayToCartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDate;

@Route("customer-charge")
public class CustomerChargeView extends VerticalLayout {
    private final RestTemplate restTemplate;

    private TextField cvv2Field;
    private TextField cartNumberField;
    private TextField amountField; // Using TextField for amount
    private DatePicker expiresDateField;
    private Integer customerId; // Store the customerId

    @Autowired
    public CustomerChargeView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        cvv2Field = new TextField("CVV2");
        cartNumberField = new TextField("Cart Number");
        amountField = new TextField("Amount");
        expiresDateField = new DatePicker("Expires Date");
        Button submitButton = new Button("Charge Credit", event -> handleSubmit());
        add(cvv2Field, cartNumberField, amountField, expiresDateField, submitButton);
        setCustomerId();
    }
    private void setCustomerId(){
        this.customerId =(Integer) VaadinSession.getCurrent().getAttribute("customerId");
    }

    private void handleSubmit() {
        String cartNumber = cartNumberField.getValue();
        String cvv2 = cvv2Field.getValue();
        Double amount = null;
        try {
            amount = Double.parseDouble(amountField.getValue());
        } catch (NumberFormatException e) {
            Notification.show("Amount must be a valid number");
            return;
        }
        LocalDate expiresDate = expiresDateField.getValue();
        PayToCartDto payToCartDto = new PayToCartDto(customerId, amount, cartNumber, cvv2, expiresDate);
        String response = restTemplate.postForObject("http://localhost:8080/customer/customerChargeCredit", payToCartDto, String.class);
        Notification.show(response);
    }
}
