package dev.kkoncki.bandup.security;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SecurityProps {

    private final Environment env;

    public SecurityProps(Environment env) {
        this.env = env;
    }

    public String getSecretKey() {
        return env.getRequiredProperty("spring.security.jwt.secret");
    }

    public long getTokenExpiration() {
        return Long.parseLong(env.getRequiredProperty("spring.security.jwt.tokenExpirationTimeInMinutes"));
    }

    public String getSecurityFilterPath() {
        return env.getRequiredProperty("spring.security.jwt.securityFilterPath");
    }
}
