package com.desafioPicPay.application.user.command;

import com.desafioPicPay.domain.user.UserRole;

public record RegisterUserCommand(
        String fullName,
        String email,
        String cpf,
        String password,
        UserRole role
) {
}
