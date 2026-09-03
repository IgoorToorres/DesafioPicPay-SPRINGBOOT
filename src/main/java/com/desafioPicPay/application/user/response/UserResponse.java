package com.desafioPicPay.application.user.response;

import com.desafioPicPay.domain.user.UserRole;

import java.math.BigDecimal;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String cpf,
        String email,
        BigDecimal balance,
        UserRole role
) {
}
