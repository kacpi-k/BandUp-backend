package dev.kkoncki.bandup.auth;

import dev.kkoncki.bandup.auth.forms.ChangePasswordForm;
import dev.kkoncki.bandup.auth.forms.CreateUserWithPasswordForm;
import dev.kkoncki.bandup.auth.forms.LoginForm;
import dev.kkoncki.bandup.auth.repository.AuthRepository;
import dev.kkoncki.bandup.auth.security.PasswordEncoder;
import dev.kkoncki.bandup.auth.security.TokenProvider;
import dev.kkoncki.bandup.auth.service.AuthService;
import dev.kkoncki.bandup.auth.service.AuthServiceImpl;
import dev.kkoncki.bandup.user.management.repository.UserManagementRepository;
import dev.kkoncki.bandup.user.management.service.UserManagementService;
import dev.kkoncki.bandup.user.management.service.UserManagementServiceImpl;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    AuthRepository authRepository = new AuthRepositoryMock();
    PasswordEncoder encoder = new PasswordEncoderMock();
    TokenProvider provider = new TokenProviderMock();

    UserManagementRepository userManagementRepository = new UserManagementRepositoryMock();
    UserManagementService userManagementService = new UserManagementServiceImpl(userManagementRepository);

    AuthService authService = new AuthServiceImpl(authRepository, encoder, provider, userManagementService);

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    private <T> void genericViolationSet(T form) {
        Set<ConstraintViolation<T>> violations = validator.validate(form);

        assertThrows(ConstraintViolationException.class, () -> {
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException("Validation failed", violations);
            }
        });
    }

    @BeforeEach
    void setup() {
        CreateUserWithPasswordForm form = new CreateUserWithPasswordForm();
        form.setFirstName("Kacper");
        form.setLastName("Koncki");
        form.setEmail("test@test.pl");
        form.setPassword("password123");
        authService.createUserWithPassword(form);
    }

    @Test
    void successfulLoginTesT() {
        LoginResponse response = authService.login(new LoginForm("test@test.pl", "password123"));

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("Bearer", response.getType());
    }

    @Test
    void userNotFoundTest() {
        assertThrows(RuntimeException.class, () -> authService.login(new LoginForm("wrongemail@test.pl", "password123")));
    }

    @Test
    void incorrectPasswordTest() {
        assertThrows(RuntimeException.class, () -> authService.login(new LoginForm("test@test.pl", "wrongpassword")));
    }

    @Test
    void changePasswordTest() {
        authService.changePassword(new ChangePasswordForm("test@test.pl", "password123", "newPassword"));

        Optional<AuthUser> user = authRepository.findByEmail("test@test.pl");
        assertTrue(user.isPresent());
        assertEquals("newPassword", user.get().getPassword());
    }

    @Test
    void validateCreateUserWithPasswordFormTest() {
        CreateUserWithPasswordForm form = new CreateUserWithPasswordForm("pass");

        genericViolationSet(form);
    }

    @Test
    void validateLogInFormTest() {
        LoginForm form = new LoginForm("test", "pass");

        genericViolationSet(form);
    }
}

