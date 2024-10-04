package org.example.service.mainService.imp;

import org.example.domain.SubHandler;
import org.example.dto.ChangeSubHandlerDto;
import org.example.exeptions.ErrorWhileUpdatingSubHandler;
import org.example.exeptions.SubHandlerNull;
import org.example.exeptions.YouInsertNothing;
import org.example.service.mainService.HandlersMainService;
import org.example.service.subHandler.SubHandlerService;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
public class HandlerMainServiceImp implements HandlersMainService {
    private final SubHandlerService subHandlerService;
    public HandlerMainServiceImp(SubHandlerService subHandlerService) {
        this.subHandlerService =subHandlerService;
    }

    @Override
    public void detailPriceSubHandlerChanger(ChangeSubHandlerDto changeSubHandlerDto) throws SubHandlerNull, ErrorWhileUpdatingSubHandler, YouInsertNothing {
        try {
            SubHandler subHandler = null;
            try {
                 subHandler = subHandlerService.findSubHandlerById(changeSubHandlerDto.id());
                if (subHandler == null) {
                    throw new SubHandlerNull();
                }
            }catch (Exception e){
                throw new SubHandlerNull();
            }
            if (!Objects.isNull(changeSubHandlerDto.detail())) {
                subHandler.setDetail(changeSubHandlerDto.detail());
            }
            if (!Objects.isNull(changeSubHandlerDto.basePrice())) {
                subHandler.setBasePrice(changeSubHandlerDto.basePrice());
            }
            if (Objects.isNull(changeSubHandlerDto.basePrice())
                    &&
                    Objects.isNull(changeSubHandlerDto.detail())) {
                throw new YouInsertNothing();
            }
            subHandlerService.updateSubHandler(subHandler);
        } catch (Exception e) {
            throw e;
        }
    }
}
