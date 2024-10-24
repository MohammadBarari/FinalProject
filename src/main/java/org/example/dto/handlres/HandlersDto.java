package org.example.dto.handlres;

import org.example.domain.SubHandler;

import java.util.List;

public record HandlersDto(String Handlers, List<SubHandler> subHandlers) {
}
