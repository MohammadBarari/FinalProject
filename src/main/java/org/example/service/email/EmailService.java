package org.example.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private JavaMailSender emailSender;
    public void sendActivationEmail(String to, String token) {
        String subject = "Email Activation";
        String activationLink = "http://localhost:8080/activate?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText("Click the link to activate your account: " + activationLink);

        emailSender.send(message);
    }
}
