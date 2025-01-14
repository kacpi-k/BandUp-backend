package dev.kkoncki.bandup.auth;

import dev.kkoncki.bandup.auth.forms.CreateUserWithPasswordForm;
import dev.kkoncki.bandup.auth.forms.LoginForm;
import dev.kkoncki.bandup.auth.service.AuthService;
import dev.kkoncki.bandup.commons.LoggedUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final LoggedUser loggedUser;

    public AuthController(AuthService authService, LoggedUser loggedUser) {
        this.authService = authService;
        this.loggedUser = loggedUser;
    }

    @PostMapping("/login")
    public LoginResponse login(LoginForm form) {
        return authService.login(form);
    }

    @PostMapping("/register")
    public void createUserWithPassword(CreateUserWithPasswordForm form) {
        authService.createUserWithPassword(form);
    }

    @PostMapping("/logout")
    public void logout() {
        authService.logout(loggedUser.getUserId());
    }
}
