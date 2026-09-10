package com.desafioPicPay.application.wallet.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record DepositResponse(
    UUID transactionId,
    UUID userId,
    BigDecimal amount,
    BigDecimal currentBalance,
    Instant createdAt
) {
}
