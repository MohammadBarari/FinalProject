package org.example.service.mainService;

import org.example.dto.ChangeSubHandlerDto;
import org.example.exeptions.ErrorWhileUpdatingSubHandler;
import org.example.exeptions.SubHandlerNull;
import org.example.exeptions.YouInsertNothing;

public interface HandlersMainService {
    void detailPriceSubHandlerChanger(ChangeSubHandlerDto changeSubHandlerDto) throws SubHandlerNull, ErrorWhileUpdatingSubHandler, YouInsertNothing;

}

