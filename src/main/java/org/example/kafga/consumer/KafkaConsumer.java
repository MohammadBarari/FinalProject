package org.example.kafga.consumer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.dto.kafkaSender.TokenMessageDto;
import org.example.enumirations.TypeOfUser;
import org.example.service.emailToken.EmailTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "token_topic", groupId = "token_group",containerFactory = "kafkaListenerContainerFactory")
    public void consumeToken(ConsumerRecord<String, TokenMessageDto> record) {
        TokenMessageDto tokenDto = record.value();
        sendingMail(tokenDto.email(),tokenDto.token(),tokenDto.typeOfUser());
    }

    private void sendingMail(String email, String token , TypeOfUser typeOfUser) {
        String activationLink = "http://localhost:8080/"+(typeOfUser == TypeOfUser.CUSTOMER? "customer" : "employee") +"/verify?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Activation");
        message.setText("Click the link to activate your account: " + activationLink);
        javaMailSender.send(message);
    }

}
