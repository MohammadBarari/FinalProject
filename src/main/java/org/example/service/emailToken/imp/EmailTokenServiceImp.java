package org.example.service.emailToken.imp;

import org.example.domain.EmailToken;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.InvalidTokenExceptions;
import org.example.repository.emailToken.EmailTokenRepository;
import org.example.service.emailToken.EmailTokenService;
import org.example.service.mainService.imp.CustomerTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailTokenServiceImp implements EmailTokenService {
    private final EmailTokenRepository emailTokenRepository;
    private final CustomerTokenService customerTokenService;
    private final JavaMailSender javaMailSender;
    @Autowired
    public EmailTokenServiceImp( EmailTokenRepository emailTokenRepository,
                                 CustomerTokenService customerTokenService,
                                 JavaMailSender javaMailSender) {
        this.emailTokenRepository = emailTokenRepository;
        this.customerTokenService = customerTokenService;
        this.javaMailSender = javaMailSender;
    }
    @Transactional
    public void remove(EmailToken emailToken) {
        emailTokenRepository.delete(emailToken);
    }

    @Override
    @Transactional
    public void sendEmail(String email , TypeOfUser typeOfUser) {
        EmailToken emailToken = createEmailToken(typeOfUser,email);
        String token = generateToken();
        emailToken.setToken(token);
        sendingMail(email,token,emailToken);
        emailTokenRepository.save(emailToken);
    }
    public EmailToken findByToken(String token) {
        return emailTokenRepository.findByToken(token);
    }
    @Override
    public void validateToken(String token) {
        //find token in database and make sure its valid
        //find time
        //find token is not expired
        //find if person is existing and person is validated
        EmailToken emailToken = Optional.ofNullable(findByToken(token)).orElseThrow(() -> new InvalidTokenExceptions("Token is invalid"));
        validateTokenProperties(emailToken);
        customerTokenService.validateUser(emailToken);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private void sendingMail(String email, String token ,EmailToken emailToken) {
        String activationLink = "http://localhost:8080/customer/verify?token=" + token;// Replace with your actual domain
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Activation");
        message.setText("Click the link to activate your account: " + activationLink);
        javaMailSender.send(message);
    }

    private void validateTokenProperties(EmailToken emailToken){
        if (emailToken.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new InvalidTokenExceptions("Time of Token is expired");
        }
        if (emailToken.isExpired()){
            throw new InvalidTokenExceptions("Token is expired");
        }
    }
    private EmailToken createEmailToken(TypeOfUser typeOfUser ,String email){
        EmailToken emailToken = new EmailToken();
        emailToken.setExpiresAt(LocalDateTime.now().plusHours(1));
        emailToken.setTypeOfUser(typeOfUser);
        emailToken.setEmail(email);
        return emailToken;
    }
}
