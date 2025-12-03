package org.example.dto.customer;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PayToCartDto(

        @NotNull
        @Digits(integer = 6, fraction = 0)
        Integer customerId,

        @NotNull
        @Digits(integer = 8, fraction = 2)
        Double amount,

        @NotNull
        @Pattern(regexp = "^\\d{16}$",
                message = "cart number must be exactly 16 digits")
        String cartNumber,

        @NotNull
        @Size(max = 4)
        @Pattern(regexp = "^[0-9]+$", message = "must be numeric")
        String cvv2,

        @NotNull
        @Future
        LocalDate expiresDate,

        @NotNull
        String captchaAnswer,

        @NotNull
        String captchaId
                           ) {}
