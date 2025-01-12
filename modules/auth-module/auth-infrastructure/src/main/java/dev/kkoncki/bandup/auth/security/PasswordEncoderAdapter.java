package dev.kkoncki.bandup.auth.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderAdapter implements PasswordEncoder {

    private final BCryptPasswordEncoder bCrypt;

    public PasswordEncoderAdapter(BCryptPasswordEncoder bCrypt) {
        this.bCrypt = bCrypt;
    }

    @Override
    public String encode(String rawText) {
        return bCrypt.encode(rawText);
    }

    @Override
    public boolean matches(String rawText, String encoded) {
        return bCrypt.matches(rawText, encoded);
    }
}
