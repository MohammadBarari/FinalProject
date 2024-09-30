package org.example.service.mainService;

import org.example.dto.ChangeSubHandlerDto;
import org.example.exeptions.SubHandlerNull;

public interface HandlersMainService {
    void detailPriceSubHandlerChanger(ChangeSubHandlerDto changeSubHandlerDto) throws SubHandlerNull;

}

