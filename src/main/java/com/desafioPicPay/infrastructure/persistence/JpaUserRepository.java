package com.desafioPicPay.infrastructure.persistence;

import com.desafioPicPay.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}
