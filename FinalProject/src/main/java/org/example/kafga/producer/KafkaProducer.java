package org.example.kafga.producer;

import lombok.RequiredArgsConstructor;
import org.example.dto.kafkaSender.TokenMessageDto;
import org.example.enumirations.TypeOfUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, TokenMessageDto> kafkaTemplate;

    public void sendToken(String email, String token , TypeOfUser typeOfUser) {
        kafkaTemplate.send("token_topic", email, new TokenMessageDto(typeOfUser,email,token));
    }
}
