package org.example.service.subHandler.imp;

import lombok.RequiredArgsConstructor;
import org.example.domain.SubHandler;
import org.example.repository.subHandler.SubHandlerRepository;
import org.example.service.subHandler.SubHandlerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SubHandlerServiceImp implements SubHandlerService {
    @Override
    public List<SubHandler> subHandlersForEmployee(Integer employeeId) {
        return subHandlerRepository.selectByEmployeeId(employeeId);
    }
    @Override
    public SubHandler findSubHandlerByName(String subHandlerName) {
        return subHandlerRepository.selectByName(subHandlerName);
    }
    private final SubHandlerRepository subHandlerRepository;
    @Override
    @Transactional
    public void saveSubHandler(SubHandler subHandler) {
        subHandlerRepository.save(subHandler);
    }

    @Override
    @Transactional
    public void deleteSubHandler(Integer subHandlerId) {
        subHandlerRepository.deleteById(subHandlerId);
    }

    @Override
    @Transactional
    public void updateSubHandler(SubHandler subHandler) {
        subHandlerRepository.save(subHandler);
    }

    @Override
    public List<SubHandler> findAllSubHandlerSameHandler(Integer handlerId) {
        return subHandlerRepository.selectBySameHandler(handlerId);
    }

    @Override
    public SubHandler findSubHandlerById(Integer subHandlerId) {
        return subHandlerRepository.findSubHandlerById(subHandlerId);
    }
}
