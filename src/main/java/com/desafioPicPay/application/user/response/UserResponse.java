package com.desafioPicPay.application.user.response;

import com.desafioPicPay.domain.user.UserRole;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String cpf,
        String email,
        UserRole role
) {
}
