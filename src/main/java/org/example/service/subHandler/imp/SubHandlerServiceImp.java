package org.example.service.subHandler.imp;

import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.repository.subHandler.SubHandlerRepository;
import org.example.repository.subHandler.imp.SubHandlerRepositoryImp;
import org.example.service.subHandler.SubHandlerService;

import java.util.List;

public class SubHandlerServiceImp implements SubHandlerService {
    SubHandlerRepository subHandlerRepository = new SubHandlerRepositoryImp();
    @Override
    public void saveSubHandler(SubHandler subHandler) {
        subHandlerRepository.save(subHandler);
    }

    @Override
    public void deleteSubHandler(Integer subHandlerId) {
        subHandlerRepository.delete(subHandlerId);
    }

    @Override
    public void updateSubHandler(SubHandler subHandler) {
        subHandlerRepository.update(subHandler);
    }

    @Override
    public List<SubHandler> findAllSubHandlerSameHandler(Handler handler) {
        return subHandlerRepository.selectBySameHandler(handler);
    }

    @Override
    public SubHandler findSubHandlerById(Integer subHandlerId) {
        return subHandlerRepository.selectById(subHandlerId);
    }
}
