package org.example.service.handler.imp;

import lombok.RequiredArgsConstructor;
import org.example.domain.Handler;
import org.example.repository.handler.HandlerRepository;
import org.example.service.handler.HandlerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class HandlerBaseImp implements HandlerService {
    private final HandlerRepository handlerRepository ;

    @Override
    @Transactional
    public void save(Handler handler) {
        handlerRepository.save(handler);
    }

    @Override
    @Transactional
    public void deleteHandler(Integer id) {
        handlerRepository.delete(id);
    }

    @Override
    @Transactional
    public void updateHandler(Handler handler) {
        handlerRepository.update(handler);
    }

    @Override
    public List<Handler> findAllHandlers() {
        return handlerRepository.findAll();
    }

    @Override
    public Handler findHandlerById(Integer id) {
        return handlerRepository.findById(id);
    }

    @Override
    public Handler findHandlerByName(String name) {
        return handlerRepository.findByName(name);
    }
}
