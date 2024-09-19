package org.example.service.handler.imp;

import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.dto.SaveSubHandlerDto;
import org.example.service.handler.HandlerService;
import org.xml.sax.HandlerBase;

import java.util.List;

public class HandlerBaseImp implements HandlerService {
    HandlerRepository handlerRepository = new HandlerRepositoryImp();


    @Override
    public void save(Handler handler) {
        handlerRepository.save(handler);
    }

    @Override
    public void deleteHandler(Integer id) {
        handlerRepository.delete(id);
    }

    @Override
    public void updateHandler(Handler handler) {
        handlerRepository.update(handler);
    }

    @Override
    public List<Handler> findAllHandlers() {
        return handlerRepository.findAll();
    }

    @Override
    public Handler findHandlerById(Integer id) {
        return handlerRepository.findByID(id);
    }
}
