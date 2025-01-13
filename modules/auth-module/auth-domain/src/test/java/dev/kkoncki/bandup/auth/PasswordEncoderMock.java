package dev.kkoncki.bandup.auth;

import dev.kkoncki.bandup.auth.security.PasswordEncoder;

import java.util.Objects;

public class PasswordEncoderMock implements PasswordEncoder {
    @Override
    public String encode(String rawText) {
        return rawText;
    }

    @Override
    public boolean matches(String rawText, String encoded) {
        return Objects.equals(rawText, encoded);
    }
}
