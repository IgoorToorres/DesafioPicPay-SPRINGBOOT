package com.desafioPicPay.controller.user.request;

import com.desafioPicPay.domain.user.UserRole;
import jakarta.validation.constraints.*;

public record UserRequest(
        @NotBlank(message = "Nome completo é obrigatório")
        @Size(max = 150, message = "Nome completo deve ter no máximo 150 caracteres")
        String fullName,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail deve possuir um formato válido")
        @Size(max = 254, message = "E-mail deve ter no máximo 254 caracteres")
        String email,

        @NotBlank(message = "CPF ou CNPJ é obrigatório")
        @Pattern(
                regexp = "\\d{11}|\\d{14}",
                message = "CPF ou CNPJ deve conter 11 ou 14 dígitos"
        )
        String document,

        @NotBlank(message = "Senha é obrigatória")
        @Size(
                min = 8,
                max = 72,
                message = "Senha deve possuir entre 8 e 72 caracteres"
        )
        String password,

        @NotNull(message = "Tipo de usuário é obrigatório")
        UserRole role
) {
}
