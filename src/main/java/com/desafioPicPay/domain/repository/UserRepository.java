package com.desafioPicPay.domain.repository;

import com.desafioPicPay.domain.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}
