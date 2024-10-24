package org.example.dto.admin;

import java.time.LocalDateTime;

public record CustomerOutput(Integer id,
                             String name,
                             String last_name,
                             String email,
                             String phone,
                             LocalDateTime timeOfRegistration,
                             boolean isActive) {
}
