package dev.kkoncki.bandup.auth.service;

import dev.kkoncki.bandup.auth.LoginResponse;
import dev.kkoncki.bandup.auth.forms.ChangePasswordForm;
import dev.kkoncki.bandup.auth.forms.CreateUserWithPasswordForm;
import dev.kkoncki.bandup.auth.forms.LoginForm;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AuthService {

    LoginResponse login(@Valid LoginForm form);
    void createUserWithPassword(@Valid CreateUserWithPasswordForm form);
    void changePassword(@Valid ChangePasswordForm form);
    void logout(String userId);
}
