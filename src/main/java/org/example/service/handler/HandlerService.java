package org.example.service.handler;

import org.example.domain.Handler;

import java.util.List;

public interface HandlerService {
    void save(Handler handler);
    void deleteHandler(Integer id);
    void updateHandler(Handler handler);
    List<Handler> findAllHandlers();
    Handler findHandlerById(Integer id);
    Handler findHandlerByName(String name);
}
