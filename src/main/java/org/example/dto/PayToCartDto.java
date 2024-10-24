package org.example.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PayToCartDto(@NotNull @Digits(integer = 3,fraction = 0) Integer customerId,
                           @NotNull @Digits(integer = 8 , fraction = 2) Double amount,
                           @NotNull @Pattern(regexp = "^\\d{16}$", message = "cart number must be exactly 16 digits") String cartNumber,
@NotNull @Size(max = 4) @Pattern(regexp = "^[0-9]+$",message = "must be numeric") String cvv2,
                           @Future LocalDate expiresDate
                           ) {}
