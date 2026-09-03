package com.desafioPicPay.controller.user.request;

import com.desafioPicPay.domain.user.UserRole;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UserRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String firstName,

        @NotBlank(message = "Sobrenome é obrigatório")
        @Size(max = 100, message = "Sobrenome deve ter no máximo 100 caracteres")
        String lastName,

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

        @NotNull(message = "Saldo é obrigatório")
        @DecimalMin(value = "0.00", message = "Saldo não pode ser negativo")
        @Digits(integer = 17, fraction = 2, message = "Saldo deve possuir no máximo duas casas decimais")
        BigDecimal balance,

        @NotNull(message = "Tipo de usuário é obrigatório")
        UserRole role
) {
}
