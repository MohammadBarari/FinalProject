package org.example.repository.handler;

import org.example.domain.Handler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HandlerRepository extends JpaRepository<Handler, Integer> {
    Handler findHandlerById(int id);
    Handler findHandlerByName(String name);
}
