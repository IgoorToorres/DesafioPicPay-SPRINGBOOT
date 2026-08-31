package com.desafioPicPay.application.user.security;

public interface PasswordHasher {
    String hash(String password);
    boolean matches(String rawPassword, String passwordHash);
}
