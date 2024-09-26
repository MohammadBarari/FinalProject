package org.example.repository.handler;

import org.example.domain.Handler;

import java.util.List;

public interface HandlerRepository {
    void save(Handler handler);
    void update(Handler handler);
    void delete(int id);
    Handler findById(int id);
    List<Handler> findAll();
}
