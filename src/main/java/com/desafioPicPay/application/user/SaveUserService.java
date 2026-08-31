package com.desafioPicPay.application.user;

import com.desafioPicPay.application.user.command.RegisterUserCommand;
import com.desafioPicPay.application.user.response.UserResponse;
import com.desafioPicPay.application.user.security.PasswordHasher;
import com.desafioPicPay.domain.repository.UserRepository;
import com.desafioPicPay.domain.user.User;
import com.desafioPicPay.error.DomainException;

public class SaveUserService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public SaveUserService(UserRepository userRepository, PasswordHasher passwordHasher){
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public UserResponse saveUser(RegisterUserCommand command){
        if(userRepository.existsByCpf(command.cpf())){
            throw new DomainException("CPF ja cadastrado");
        }
        if(userRepository.existsByEmail(command.email())){
            throw new DomainException("E-mail ja cadastrado");
        }

        String passwordHashed = passwordHasher.hash(command.password());

        User user = new User(
                command.fullName(),
                command.cpf(),
                command.email(),
                passwordHashed,
                command.role()
        );

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    private UserResponse toResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getCpf(),
                user.getEmail(),
                user.getRole()
        );
    }
}
