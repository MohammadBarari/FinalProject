package org.example.dto.servisesDone;

import org.example.domain.SubHandler;
import org.example.dto.subHandlers.SubHandlersDtoOutput;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DoneDutiesDto(LocalDateTime timeOfDuty, Double price, SubHandlersDtoOutput subHandler, Integer employeeScore, Integer employeeId, String employeeFullName, String customerFullName , Integer customerId, String comment) {
}
