package dev.kkoncki.bandup.auth.repository;

import dev.kkoncki.bandup.auth.AuthUser;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthRepositoryAdapter implements AuthRepository {
    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<AuthUser> findById(String id) {
        return Optional.empty();
    }

    @Override
    public void save(AuthUser user) {

    }
}
