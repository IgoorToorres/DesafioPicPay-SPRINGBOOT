package com.desafioPicPay.domain.user;

import com.desafioPicPay.error.DomainException;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "cpf", nullable = false, updatable = false, unique = true)
    private String cpf;

    @Column(name = "email", nullable = false, updatable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    protected User(){}

    public User(
            String firstName,
            String lastName,
            String cpf,
            String email,
            String password,
            BigDecimal balance,
            UserRole role
    ){
        validateNullString(firstName, "nome é obrigatório");
        validateNullString(lastName, "nome completo é obrigatório");
        validateNullString(cpf, "cpf é obrigatório");
        validateNullString(email, "email é obrigatório");
        validateNullString(password, "senha é obrigatória");
        validateBalance(balance);

        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.role = role;
    }

    private void validateNullString(String value, String message){
        if(value == null || value.isBlank()){
            throw new DomainException(message);
        }
    }

    private void validateBalance(BigDecimal balance) {
        if (balance == null) {
            throw new DomainException("Saldo é obrigatório");
        }

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Saldo não pode ser negativo");
        }
    }

    public void credit(BigDecimal amount){
        validatePositiveAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount){
        validatePositiveAmount(amount);
        if(balance.compareTo(amount) < 0){
            throw new DomainException("saldo insuficiente");
        }

        this.balance = balance.subtract(amount);
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("O valor deve ser maior que zero");
        }
    }
}
