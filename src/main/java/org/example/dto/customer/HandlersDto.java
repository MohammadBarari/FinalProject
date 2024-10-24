package org.example.dto.customer;

import org.example.domain.SubHandler;

import java.util.List;

public record HandlersDto(String Handlers, List<SubHandler> subHandlers) {
}
