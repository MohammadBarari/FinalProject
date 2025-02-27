package org.example.dto.kafkaSender;

import org.example.enumirations.TypeOfUser;

public record TokenMessageDto(TypeOfUser typeOfUser , String email , String token) {
}
