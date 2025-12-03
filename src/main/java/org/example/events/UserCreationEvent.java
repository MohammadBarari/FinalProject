package org.example.events;

import org.example.enumirations.TypeOfUser;

public record UserCreationEvent(TypeOfUser typeOfCreatedUser, String email) {
}
