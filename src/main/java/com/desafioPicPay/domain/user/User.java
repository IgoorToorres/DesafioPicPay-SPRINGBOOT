package com.desafioPicPay.domain.user;

import com.desafioPicPay.error.DomainException;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "fullName", nullable = false)
    private String fullName;

    @Column(name = "cpf", nullable = false, updatable = false, unique = true)
    private String cpf;

    @Column(name = "email", nullable = false, updatable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    protected User(){}

    public User(
            String fullName, String cpf, String email, String password, UserRole role
    ){
        validateNullString(fullName, "Nome completo é obrigatório");
        validateNullString(cpf, "cpf é obrigatório");
        validateNullString(email, "email é obrigatório");
        validateNullString(password, "senha é obrigatória");

        this.id = UUID.randomUUID();
        this.fullName = fullName;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void validateNullString(String value, String message){
        if(value == null || value.isBlank()){
            throw new DomainException(message);
        }
    }
}
