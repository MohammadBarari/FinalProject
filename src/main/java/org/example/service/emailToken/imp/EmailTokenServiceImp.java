package org.example.service.emailToken.imp;

import org.example.domain.EmailToken;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.InvalidTokenExceptions;
import org.example.repository.emailToken.EmailTokenRepository;
import org.example.service.emailToken.EmailTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailTokenServiceImp implements EmailTokenService {
    private final EmailTokenRepository emailTokenRepository;

    private final JavaMailSender javaMailSender;
    @Autowired
    public EmailTokenServiceImp( EmailTokenRepository emailTokenRepository,

                                 JavaMailSender javaMailSender) {
        this.emailTokenRepository = emailTokenRepository;

        this.javaMailSender = javaMailSender;
    }
    @Transactional
    public void remove(EmailToken emailToken) {
        emailTokenRepository.delete(emailToken);
    }
    //update
    @Override
    @Transactional
    public void sendEmail(String email , TypeOfUser typeOfUser) {
        if (emailTokenRepository.findByEmail(email) != null){
            throw new InvalidTokenExceptions("Email already exists");
        }
        EmailToken emailToken = createEmailToken(typeOfUser,email);
        String token = generateToken();
        emailToken.setToken(token);
        sendingMail(email,token,emailToken,typeOfUser);
        emailTokenRepository.save(emailToken);
    }
    @Override
    public EmailToken findByToken(String token) {
        return emailTokenRepository.findByToken(token);
    }
    @Override
    public void validateToken(String token) {
        EmailToken emailToken = Optional.ofNullable(findByToken(token)).orElseThrow(() -> new InvalidTokenExceptions("Token is invalid"));
        validateTokenProperties(emailToken);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private void sendingMail(String email, String token ,EmailToken emailToken,TypeOfUser typeOfUser) {
        emailTokenRepository.findByEmail(email);
        String activationLink = "http://localhost:8080/"+(typeOfUser == TypeOfUser.CUSTOMER? "customer" : "employee") +"/verify?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Activation");
        message.setText("Click the link to activate your account: " + activationLink);
        javaMailSender.send(message);
    }

    private void validateTokenProperties(EmailToken emailToken){
        if (emailToken.getExpiresAt().isBefore(LocalDateTime.now())) {
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
