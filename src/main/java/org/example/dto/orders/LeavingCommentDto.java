package org.example.dto.orders;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PathVariable;

public record LeavingCommentDto( @NotNull @Digits(integer = 5, fraction = 0) Integer ordersId,
                                 @NotNull @Digits(integer = 5, fraction = 0) Integer star,
                                 @NotNull String comment) {
}
