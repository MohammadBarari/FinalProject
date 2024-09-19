package org.example.service.subHandler;

import org.example.domain.Handler;
import org.example.domain.SubHandler;

import java.util.List;

public interface SubHandlerService {
    void saveSubHandler(SubHandler subHandler);
    void deleteSubHandler(Integer subHandlerId);
    void updateSubHandler(SubHandler subHandler);
    List<SubHandler> findAllSubHandlerSameHandler(Handler handler);
    SubHandler findSubHandlerById(Integer subHandlerId);

}
