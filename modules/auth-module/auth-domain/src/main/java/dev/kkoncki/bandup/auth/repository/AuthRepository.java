package dev.kkoncki.bandup.auth.repository;

import dev.kkoncki.bandup.auth.AuthUser;

import java.util.Optional;

public interface AuthRepository {

    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findById(String id);
    void save(AuthUser user);
}
