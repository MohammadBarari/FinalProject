package org.example.dto.admin;

import java.time.LocalDate;
import java.util.List;

public record FindFilteredOrdersDto(LocalDate startDate
        ,
                                    LocalDate endDate
        ,
                                    List<String> handlersName
        ,
                                    List<String> subHandlersName) {
}
