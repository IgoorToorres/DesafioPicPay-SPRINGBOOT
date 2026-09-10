package com.desafioPicPay.application.wallet.command;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawCommand(
        UUID userId,
        BigDecimal amount
) {
}
