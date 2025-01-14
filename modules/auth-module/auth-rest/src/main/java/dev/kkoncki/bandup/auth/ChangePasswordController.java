package dev.kkoncki.bandup.auth;

import dev.kkoncki.bandup.auth.forms.ChangePasswordForm;
import dev.kkoncki.bandup.auth.service.AuthService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password")
public class ChangePasswordController {

    private final AuthService authService;

    public ChangePasswordController(AuthService authService) {
        this.authService = authService;
    }

    @PutMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordForm form) {
        authService.changePassword(form);
    }
}
