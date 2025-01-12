package dev.kkoncki.bandup.auth.security;

public interface TokenProvider {
    String generateToken(String userId);
}
