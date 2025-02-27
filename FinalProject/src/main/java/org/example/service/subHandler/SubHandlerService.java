package org.example.service.subHandler;

import org.example.domain.SubHandler;

import java.util.List;

public interface SubHandlerService {
    void saveSubHandler(SubHandler subHandler);
    void deleteSubHandler(Integer subHandlerId);
    void updateSubHandler(SubHandler subHandler);
    List<SubHandler> findAllSubHandlerSameHandler(Integer handlerId);
    SubHandler findSubHandlerById(Integer subHandlerId);
    List<SubHandler> subHandlersForEmployee(Integer employeeId);
    SubHandler findSubHandlerByName(String subHandlerName);
}
