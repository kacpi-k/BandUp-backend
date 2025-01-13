package dev.kkoncki.bandup.auth;

import dev.kkoncki.bandup.auth.security.TokenProvider;

import java.util.UUID;

public class TokenProviderMock implements TokenProvider {
    @Override
    public String generateToken(String userId) {
        return UUID.randomUUID().toString();
    }
}
