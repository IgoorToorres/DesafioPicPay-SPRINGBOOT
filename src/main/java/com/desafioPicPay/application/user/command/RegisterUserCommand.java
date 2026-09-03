package com.desafioPicPay.application.user.command;

import com.desafioPicPay.domain.user.UserRole;

import java.math.BigDecimal;

public record RegisterUserCommand(
        String firstName,
        String lastName,
        String email,
        String cpf,
        String password,
        BigDecimal balance,
        UserRole role
) {
}
