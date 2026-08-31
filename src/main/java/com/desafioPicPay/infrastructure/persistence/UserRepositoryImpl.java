package com.desafioPicPay.infrastructure.persistence;

import com.desafioPicPay.domain.repository.UserRepository;
import com.desafioPicPay.domain.user.User;

import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository){
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return jpaUserRepository.existsByCpf(cpf);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }
}
