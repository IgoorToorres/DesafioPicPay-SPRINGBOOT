package com.desafioPicPay.controller.wallet.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepositRequest(

        @NotNull(message = "O valor da transação é obrigatório")
        @Positive(message = "O valor da transação deve ser maior que zero")
        @Digits(
                integer = 17,
                fraction = 2,
                message = "O valor deve possuir no máximo duas casas decimais"
        )
        BigDecimal amount
) {
}
