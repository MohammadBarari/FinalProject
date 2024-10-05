package org.example.java.org.example;

import org.example.domain.SubHandler;
import org.example.dto.ChangeSubHandlerDto;
import org.example.exeptions.ErrorWhileUpdatingSubHandler;
import org.example.exeptions.SubHandlerNull;
import org.example.exeptions.YouInsertNothing;
import org.example.service.mainService.imp.HandlerMainServiceImp;
import org.example.service.subHandler.SubHandlerService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HandlerServiceImpTest {
    @Mock
    private SubHandlerService subHandlerService;

    @InjectMocks
    private HandlerMainServiceImp handlerMainService;

    @BeforeEach
    void setUp()  {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testDetailPriceSubHandlerChanger_UpdatesDetailAndBasePrice() throws SubHandlerNull, SubHandlerNull, ErrorWhileUpdatingSubHandler, YouInsertNothing {
        ChangeSubHandlerDto dto = new ChangeSubHandlerDto(1, "new detail", 100.0);
        SubHandler subHandler = new SubHandler();
        when(subHandlerService.findSubHandlerById(1)).thenReturn(subHandler);
        handlerMainService.detailPriceSubHandlerChanger(dto);
        assertEquals("new detail", subHandler.getDetail());
        assertEquals(100.0, subHandler.getBasePrice());
        verify(subHandlerService).updateSubHandler(subHandler);
    }

    @Test
    void testDetailPriceSubHandlerChanger_OnlyUpdatesDetail() throws SubHandlerNull, ErrorWhileUpdatingSubHandler, YouInsertNothing {

        ChangeSubHandlerDto dto = new ChangeSubHandlerDto(1, "new detail", null);
        SubHandler subHandler = new SubHandler();
        when(subHandlerService.findSubHandlerById(1)).thenReturn(subHandler);
        handlerMainService.detailPriceSubHandlerChanger(dto);
        assertEquals("new detail", subHandler.getDetail());
        assertNull(subHandler.getBasePrice());
        verify(subHandlerService).updateSubHandler(subHandler);
    }

    @Test
    void testDetailPriceSubHandlerChanger_ThrowsYouInsertNothing() {
        ChangeSubHandlerDto dto = new ChangeSubHandlerDto(1, null, null);
        when(subHandlerService.findSubHandlerById(1)).thenReturn(new SubHandler());
        assertThrows(YouInsertNothing.class, () -> {
            handlerMainService.detailPriceSubHandlerChanger(dto);
        });
    }

    @Test
    void testDetailPriceSubHandlerChanger_ThrowsSubHandlerNull() {
        ChangeSubHandlerDto dto = new ChangeSubHandlerDto(1, "detail", 50.0);
        when(subHandlerService.findSubHandlerById(1)).thenThrow(new RuntimeException());
        assertThrows(SubHandlerNull.class, () -> {
            handlerMainService.detailPriceSubHandlerChanger(dto);
        });
    }


}
