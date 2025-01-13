package dev.kkoncki.bandup.auth;

import dev.kkoncki.bandup.auth.repository.AuthRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthRepositoryMock implements AuthRepository {
    Map<String, AuthUser> mockDB = new HashMap<>();

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return Optional.ofNullable(mockDB.get(email));
    }

    @Override
    public Optional<AuthUser> findById(String id) {
        return Optional.ofNullable(mockDB.get(id));
    }

    @Override
    public void save(AuthUser user) {
        mockDB.put(user.getEmail(), user);
    }
}
