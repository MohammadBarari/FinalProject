package org.example.service.emailToken.imp;
import org.example.kafga.consumer.KafkaConsumer;
import org.example.kafga.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.example.domain.EmailToken;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.emailToken.InvalidTokenExceptions;
import org.example.repository.emailToken.EmailTokenRepository;
import org.example.service.emailToken.EmailTokenService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailTokenServiceImp implements EmailTokenService {

    private final EmailTokenRepository repository;

    private final KafkaProducer tokenSenderService;
    private final KafkaConsumer sendEmailService;

    @Transactional
    public void remove(EmailToken emailToken) {
        repository.delete(emailToken);
    }

    @Override
    @Transactional
    public void sendEmail(String email , TypeOfUser typeOfUser) {

        Optional.of(repository.existsByEmail(email)).filter(f -> !f).orElseThrow(()  -> new InvalidTokenExceptions("Email already exists"));

        EmailToken emailToken = createEmailToken(typeOfUser,email);

        String token = generateToken();

        emailToken.setToken(token);

        tokenSenderService.sendToken(emailToken.getEmail(),emailToken.getToken(),typeOfUser);

        repository.save(emailToken);
    }

    @Override
    public EmailToken findByToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    public void validateToken(String token) {
        EmailToken emailToken = Optional.ofNullable(findByToken(token)).orElseThrow(() -> new InvalidTokenExceptions("Token is invalid"));
        validateTokenProperties(emailToken);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

//    private void sendingMail(String email, String token ,TypeOfUser typeOfUser) {
//        String activationLink = "http://localhost:8080/"+(typeOfUser == TypeOfUser.CUSTOMER? "customer" : "employee") +"/verify?token=" + token;
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Email Activation");
//        message.setText("Click the link to activate your account: " + activationLink);
//    }

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
