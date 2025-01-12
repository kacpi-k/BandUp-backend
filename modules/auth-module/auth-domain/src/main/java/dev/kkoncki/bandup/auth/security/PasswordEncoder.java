package dev.kkoncki.bandup.auth.security;

public interface PasswordEncoder {
    String encode(String rawText);
    boolean matches(String rawText, String encoded);
}
