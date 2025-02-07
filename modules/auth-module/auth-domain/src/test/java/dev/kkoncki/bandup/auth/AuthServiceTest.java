package dev.kkoncki.bandup.auth;

import dev.kkoncki.bandup.auth.forms.ChangePasswordForm;
import dev.kkoncki.bandup.auth.forms.CreateUserWithPasswordForm;
import dev.kkoncki.bandup.auth.forms.LoginForm;
import dev.kkoncki.bandup.auth.repository.AuthRepository;
import dev.kkoncki.bandup.auth.security.PasswordEncoder;
import dev.kkoncki.bandup.auth.security.TokenProvider;
import dev.kkoncki.bandup.auth.service.AuthServiceImpl;
import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.user.management.User;
import dev.kkoncki.bandup.user.management.service.UserManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private UserManagementService userManagementService;

    @InjectMocks
    private AuthServiceImpl authService;

    private AuthUser authUser;
    private User user;
    private LoginForm loginForm;
    private CreateUserWithPasswordForm createUserForm;
    private ChangePasswordForm changePasswordForm;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser();
        authUser.setId("user-id");
        authUser.setEmail("test@test.com");
        authUser.setPassword("encoded-password");

        user = new User();
        user.setId("user-id");
        user.setBlocked(false);

        loginForm = new LoginForm();
        loginForm.setEmail("test@test.com");
        loginForm.setPassword("password123");

        createUserForm = new CreateUserWithPasswordForm();
        createUserForm.setEmail("test@test.com");
        createUserForm.setPassword("password123");

        changePasswordForm = new ChangePasswordForm();
        changePasswordForm.setEmail("test@test.com");
        changePasswordForm.setOldPassword("old-password");
        changePasswordForm.setNewPassword("new-password");
    }

    @Test
    void shouldLoginSuccessfully() {
        when(authRepository.findByEmail("test@test.com")).thenReturn(Optional.of(authUser));
        when(userManagementService.get("user-id")).thenReturn(user);
        when(passwordEncoder.matches("password123", "encoded-password")).thenReturn(true);
        when(tokenProvider.generateToken("user-id")).thenReturn("jwt-token");

        LoginResponse response = authService.login(loginForm);

        assertNotNull(response);
        assertEquals("jwt-token", response.token);
        assertEquals("Bearer", response.type);
        verify(authRepository).findByEmail("test@test.com");
        verify(passwordEncoder).matches("password123", "encoded-password");
        verify(tokenProvider).generateToken("user-id");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnLogin() {
        when(authRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () ->
                authService.login(loginForm)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenUserIsBlocked() {
        user.setBlocked(true);
        when(authRepository.findByEmail("test@test.com")).thenReturn(Optional.of(authUser));
        when(userManagementService.get("user-id")).thenReturn(user);

        ApplicationException exception = assertThrows(ApplicationException.class, () ->
                authService.login(loginForm)
        );

        assertEquals(ErrorCode.USER_BLOCKED, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionOnInvalidPassword() {
        when(authRepository.findByEmail("test@test.com")).thenReturn(Optional.of(authUser));
        when(userManagementService.get("user-id")).thenReturn(user);
        when(passwordEncoder.matches("password123", "encoded-password")).thenReturn(false);

        ApplicationException exception = assertThrows(ApplicationException.class, () ->
                authService.login(loginForm)
        );

        assertEquals(ErrorCode.INVALID_CREDENTIALS, exception.getErrorCode());
    }

    @Test
    void shouldCreateUserWithPassword() {
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(userManagementService.save(any(CreateUserWithPasswordForm.class)))
                .thenReturn(user);

        authService.createUserWithPassword(createUserForm);

        verify(authRepository).save(any(AuthUser.class));
        verify(passwordEncoder).encode("password123");
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        when(authRepository.findByEmail("test@test.com")).thenReturn(Optional.of(authUser));
        when(passwordEncoder.matches("old-password", "encoded-password")).thenReturn(true);
        when(passwordEncoder.encode("new-password")).thenReturn("encoded-new-password");

        authService.changePassword(changePasswordForm);

        assertEquals("encoded-new-password", authUser.getPassword());
        verify(authRepository).save(authUser);
    }

    @Test
    void shouldThrowExceptionOnWrongOldPassword() {
        when(authRepository.findByEmail("test@test.com")).thenReturn(Optional.of(authUser));
        when(passwordEncoder.matches("old-password", "encoded-password")).thenReturn(false);

        ApplicationException exception = assertThrows(ApplicationException.class, () ->
                authService.changePassword(changePasswordForm)
        );

        assertEquals(ErrorCode.WRONG_PASSWORD, exception.getErrorCode());
    }
}


