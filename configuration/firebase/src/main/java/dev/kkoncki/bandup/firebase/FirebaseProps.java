package dev.kkoncki.bandup.firebase;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class FirebaseProps {

    private final Environment env;

    public FirebaseProps(Environment env) {
        this.env = env;
    }

    public String getBucket() {
        return env.getRequiredProperty("app.firebase.bucket");
    }
}
