package org.example.service.mainService.imp;

import org.example.domain.SubHandler;
import org.example.dto.ChangeSubHandlerDto;
import org.example.exeptions.ErrorWhileUpdatingSubHandler;
import org.example.exeptions.SubHandlerNull;
import org.example.exeptions.YouInsertNothing;
import org.example.service.handler.HandlerService;
import org.example.service.mainService.HandlersMainService;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.subHandler.imp.SubHandlerServiceImp;

import java.util.Objects;

public class HandlerMainServiceImp implements HandlersMainService {
    private final SubHandlerService subHandlerService;
    public HandlerMainServiceImp() {
        subHandlerService = new SubHandlerServiceImp();
    }

    @Override
    public void detailPriceSubHandlerChanger(ChangeSubHandlerDto changeSubHandlerDto) throws SubHandlerNull {
        try {
            SubHandler subHandler = subHandlerService.findSubHandlerById(changeSubHandlerDto.id());
            try {
                if (!Objects.isNull(changeSubHandlerDto.detail())){
                    subHandler.setDetail(changeSubHandlerDto.detail());
                }
                if (Objects.isNull(changeSubHandlerDto.basePrice())){
                    subHandler.setBasePrice(changeSubHandlerDto.basePrice());
                }
                if (Objects.isNull(changeSubHandlerDto.basePrice())
                &&
                        Objects.isNull(changeSubHandlerDto.detail())){
                    throw new YouInsertNothing();
                }
                subHandlerService.updateSubHandler(subHandler);
            }catch (Exception e){
                throw new ErrorWhileUpdatingSubHandler();
            }
        }catch (Exception e) {
            throw new SubHandlerNull();
        }

    }

}
